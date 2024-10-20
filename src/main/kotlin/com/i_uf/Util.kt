package com.i_uf

import java.awt.image.BufferedImage
import kotlin.math.*
fun read16x16(image: BufferedImage): Array<String> {
    val result = Array(2){""}
    for (i in 0 until 16) {
        for (j in 0 until 16) {
            val pixel = image.getRGB(j, i)
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF
            result[i / 8] += findClosestColor(red, green, blue).text
        }
        result[i / 8] += "\n"
    }
    return result
}
fun read16x16C(image: BufferedImage): Array<Array<Color>> {
    val result = Array(16){Array(16){ Color("", 0, 0,0) } }
    for (i in 0 until 16) {
        for (j in 0 until 16) {
            val pixel = image.getRGB(j, i)
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF
            result[j][i] = findClosestColor(red, green, blue)
        }
    }
    return result
}
fun colorDistance(c1: Color, r: Int, g: Int, b: Int): Double {
    return sqrt((c1.r - r).toDouble().pow(2) +
            (c1.g - g).toDouble().pow(2) +
            (c1.b - b).toDouble().pow(2))
}
fun findClosestColor(red: Int, green: Int, blue: Int): Color {
    if(mode == 2) {
        return Color(":%X%X%X:".format((min(red + 8, 0xFF) ) shr 4,
            min(green + 8, 0xFF) shr 4, min(blue + 8, 0xFF) shr 4),
            min(red + 8, 0xFF) and 0xF0, min(green + 8, 0xFF)
                    and 0xF0, min(blue + 8, 0xFF) and 0xF0)
    }
    val colorList = if(mode == 0) color4 else color12
    var closestColor = Color(":000000:", 0, 0,0)
    var minDistance = colorDistance(closestColor, red, green, blue)

    for (color in colorList) {
        val distance = colorDistance(color, red, green, blue)
        if (distance < minDistance) {
            closestColor = color
            minDistance = distance
        }
    }

    return closestColor
}
data class Color(val text: String, val r: Int, val g: Int, val b: Int) {
    fun rgb() = r shl 16 or (g shl 8) or b
    operator fun invoke(other: Color) = rgb() == other.rgb() || text == other.text
}