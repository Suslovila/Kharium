package com.suslovila.kharium.utils

import net.minecraft.util.MathHelper
import java.awt.Color
import java.util.*
import kotlin.Pair
import kotlin.collections.ArrayList

object SusUtils {
    //some static fields and functions to make life easier ;)

    var random = Random()
    const val aspectReducePerTick = 3
    const val antiNodeAppearanceTime = 120
    const val humilitasColor = 16727457
    val humilitasColorObj = Color(humilitasColor)
    //blocks per tick (for radius)
    const val explosionSpreadSpeed : Double = 1.0
    const val blockAmountDestroyedPerTick = 100000
    

@JvmStatic
fun getCordSystemFromVec3(vec3: SusVec3) : ArrayList<SusVec3> {
    val orthogonal = getOrthogonalVec3(vec3)
    return arrayListOf(vec3.cross(orthogonal).normalize(), vec3.normalize(), orthogonal.normalize())
}

    fun <T> ArrayList<T>.setAndGet(pos : Int, element : T) : ArrayList<T> {
        this[pos] = element
        return this
    }
    fun <T> ArrayList<T>.addAndGet(element : T) : ArrayList<T> {
        this.add(element)
        return this
    }

    @JvmStatic
    fun getOrthogonalVec3(vec3 : SusVec3) : SusVec3 {
        val orthogonal : SusVec3
        val vec3Cords = vec3.cordsAsList
        //Int - the "pos" bounded to x,y,z
        //Double - the value
        val vec3CordsWithIndexes : ArrayList<Pair<Int, Double>>  = arrayListOf()
        for(i in vec3Cords.indices){
            vec3CordsWithIndexes.add(Pair(i, vec3Cords[i]))
        }
        val notNullVec3Cords = vec3CordsWithIndexes.filter { it.second != 0.0 }
        when(notNullVec3Cords.size) {
            3 -> orthogonal = SusVec3(-vec3.z, 0.0, vec3.x)
            2 -> {
                with(notNullVec3Cords) {
                    //swaps the 2 cords of vec3 and makes one of them opposite. The last 3rd cord stays zero. Now vec3 DOT orthogonal = 0 ---> there is angle 90 degrees between them
                    orthogonal = SusVec3.getVectorFromArrayList(arrayListOf(0.0,0.0,0.0).setAndGet(this[1].first, this[0].second).setAndGet(this[0].first, -this[1].second))
                }
            }
            // if only 1 cord is non-zero - we just put it to another's one place (x becomes y, y becomes z, z becomes x), and the other cords stay zero
            1 -> orthogonal = SusVec3.getVectorFromArrayList(arrayListOf(0.0,0.0,0.0).setAndGet((notNullVec3Cords[0].first + 1) % 3, notNullVec3Cords[0].second))

            else -> orthogonal = SusVec3(0.0,0.0,0.0)
        }
        return orthogonal.normalize()
    }

    fun getSphereShapeCords(center : SusVec3, radius : Double) : ArrayList<SusVec3> {
        val radiusInt = radius.toInt()
        val roundedCenter = SusVec3(center.x.toInt(), center.y.toInt(), center.z.toInt())
        val res = ArrayList<SusVec3>()
        for(x0 in (-radiusInt)..radiusInt){
            for(y0 in (-radiusInt)..radiusInt){
                for(z0 in (-radiusInt)..radiusInt){
                    val iterablePos = SusVec3(roundedCenter.x + x0,roundedCenter.y + y0,roundedCenter.z + z0)
                    if(center.subtract(iterablePos).length() <= radius){
                        res.add(iterablePos)
                    }
                }
            }
        }
        return res
    }
}

//basic functions, take angle in radians
fun sin(value : Double) = MathHelper.sin(value.toFloat())
fun cos(value: Double) = MathHelper.cos(value.toFloat())
