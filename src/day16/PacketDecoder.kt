package day16

import utils.hexToBinaryString
import java.math.BigInteger

abstract class Packet(rawBinaryString: String) {
    protected val version = rawBinaryString.take(3).toInt(radix = 2)
    protected val typeId = parseType(rawBinaryString)
    protected val rawPayload = rawBinaryString.drop(6)

    protected var packets: List<Packet>? = null
    protected var literalValue: BigInteger? = null
    lateinit var unusedBits: String

    fun versionSum(): Int {
        return version + (packets?.sumBy { it.versionSum() } ?: 0)
    }

    abstract fun value(): BigInteger

    companion object {
        fun parseType(rawBinaryString: String): Int {
            return rawBinaryString.drop(3).take(3).toInt(radix = 2)
        }

        fun parseBinaryString(binaryString: String): Packet {
            return when (parseType(binaryString)) {
                4 -> {
                    LiteralValuePacket(binaryString)
                }
                else -> {
                    OperatorPacket(binaryString)
                }
            }
        }
    }
}

class LiteralValuePacket(
        rawBinaryString: String
) : Packet(rawBinaryString) {
    init {
        var payloadBinaryString = ""
        var remainingPayload = rawPayload

        while (remainingPayload.isNotEmpty()) {
            val nextChar = remainingPayload.take(1)
            remainingPayload = remainingPayload.drop(1)

            val nextNibble = remainingPayload.take(4)
            remainingPayload = remainingPayload.drop(4)

            payloadBinaryString += nextNibble

            if (nextChar == "0") break
        }

        literalValue = payloadBinaryString.toBigInteger(radix = 2)
        unusedBits = remainingPayload
    }

    override fun value(): BigInteger {
        return literalValue!!
    }
}

class OperatorPacket(
        rawBinaryString: String
) : Packet(rawBinaryString) {
    init {
        val lengthTypeId = rawPayload.take(1)
        val remainingPayload = rawPayload.drop(1)

        val parsedPayload = if (lengthTypeId == "0") {
            parseTotalLengthPackets(remainingPayload)
        } else {
            parseNumberOfPackets(remainingPayload)
        }

        packets = parsedPayload.first
        unusedBits = parsedPayload.second
    }

    companion object {
        private fun parseTotalLengthPackets(payload: String): Pair<List<Packet>, String> {
            val lengthOfPackets = payload.take(15).toInt(radix = 2)
            var remainingPayload = payload.drop(15)
            val unusedBits = remainingPayload.drop(lengthOfPackets)

            val packetList = mutableListOf<Packet>()

            remainingPayload = remainingPayload.take(lengthOfPackets)
            while (remainingPayload.isNotEmpty()) {
                val newPacket = parseBinaryString(remainingPayload)

                remainingPayload = newPacket.unusedBits

                packetList.add(newPacket)
            }

            return Pair(packetList, unusedBits)
        }

        private fun parseNumberOfPackets(payload: String): Pair<List<Packet>, String> {
            val numberOfPackets = payload.take(11).toInt(radix = 2)
            var remainingPayload = payload.drop(11)

            val packetList = mutableListOf<Packet>()

            while (packetList.size < numberOfPackets) {
                if (remainingPayload.isEmpty()) throw Exception("ran out of bits before desired number of packets")
                val newPacket = parseBinaryString(remainingPayload)

                remainingPayload = newPacket.unusedBits

                packetList.add(newPacket)
            }

            return Pair(packetList, packetList.last().unusedBits)
        }
    }

    override fun value(): BigInteger {
        return when (typeId) {
            0 -> {
                var value = 0.toBigInteger()
                for (packet in packets!!) {
                    value += packet.value()
                }
                value
            }
            1 -> {
                var value = 1.toBigInteger()
                for (packet in packets!!) {
                    value *= packet.value()
                }
                value
            }
            2 -> {
                packets!!.map { it.value() }.min()!!
            }
            3 -> {
                packets!!.map { it.value() }.max()!!
            }
            5 -> {
                if (packets?.size != 2) {
                    throw IllegalStateException("greater than packets require 2 subpackets, found: ${packets?.size}")
                }
                if (packets!![0].value() > packets!![1].value()) {
                    1.toBigInteger()
                } else {
                    0.toBigInteger()
                }
            }
            6 -> {
                if (packets?.size != 2) {
                    throw IllegalStateException("less than packets require 2 subpackets, found: ${packets?.size}")
                }
                if (packets!![0].value() < packets!![1].value()) {
                    1.toBigInteger()
                } else {
                    0.toBigInteger()
                }
            }
            7 -> {
                if (packets?.size != 2) {
                    throw IllegalStateException("equal to packets require 2 subpackets, found: ${packets?.size}")
                }
                if (packets!![0].value() == packets!![1].value()) {
                    1.toBigInteger()
                } else {
                    0.toBigInteger()
                }
            }
            else -> throw Exception("unrecognized operator packet type: $typeId")
        }
    }
}

fun solvePuzzle1(input: List<String>) {
    val binaryStringInput = input.first().hexToBinaryString()

    val result = Packet.parseBinaryString(binaryStringInput).versionSum()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val binaryStringInput = input.first().hexToBinaryString()

    val result = Packet.parseBinaryString(binaryStringInput).value()

    println(result)
}
