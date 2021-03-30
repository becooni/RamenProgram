package example

import kotlin.math.roundToLong


class RamenProgram {

    companion object {
        private const val RAMEN_COUNT = 5

        @JvmStatic
        fun main(args: Array<String>) {
            val ramenCook = RamenCook(RAMEN_COUNT)
            Thread(ramenCook, "A").start()
            Thread(ramenCook, "B").start()
            Thread(ramenCook, "C").start()
            Thread(ramenCook, "D").start()
        }
    }

    class RamenCook(private var ramenCount: Int) : Runnable {

        private val burners = mutableListOf("_", "_", "_", "_")

        override fun run() {
            while (ramenCount > 0) {

                synchronized(this) {
                    ramenCount--
                    println("${Thread.currentThread().name}: ${ramenCount}개 남음")
                }

                for (i in burners.indices) {
                    if ((burners[i] == "_").not()) continue

                    synchronized(this) {
                        burners[i] = Thread.currentThread().name
                        println("            ${Thread.currentThread().name}: [${i + 1}]번 버너 ON")
                        showBurners()
                    }

                    Thread.sleep(2000)

                    synchronized(this) {
                        burners[i] = "_"
                        println("            ${Thread.currentThread().name}: [${i + 1}]번 버너 OFF")
                        showBurners()
                    }
                    break
                }

                Thread.sleep((1000 * Math.random()).roundToLong())
            }
        }

        private fun showBurners() {
            var stringToPrint = ""

            for (i in burners.indices) {
                stringToPrint += " ${burners[i]}"
            }

            println(stringToPrint)
        }
    }
}