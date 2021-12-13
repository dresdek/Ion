package net.starlegacy.feature.starship.active

import net.starlegacy.util.Vec3i
import net.starlegacy.util.blockKeyX
import net.starlegacy.util.blockKeyY
import net.starlegacy.util.blockKeyZ

class ActiveStarshipHitbox(blocks: Set<Long>) {
	/**
	 * First dimension: array of z axes
	 * Second dimension: Array of y axis bounds
	 * Third dimension: arrays with length 2, 0 is min y, 1 is max y
	 */
	private var boundsArray: Array<Array<IntArray?>?> = arrayOfNulls(0)
	var min = Vec3i(0, 0, 0)
		private set
	var max = Vec3i(0, 0, 0)
		private set

	init {
		calculate(blocks)
	}

	fun calculate(blocks: Set<Long>) {
		calculateMinMax(blocks)
		calculateBounds(blocks)
	}

	fun calculateBounds(blocks: Set<Long>) {
		if (blocks.isEmpty()) {
			min = Vec3i(0, 0, 0)
			max = Vec3i(0, 0, 0)
			return
		}
		val minX = min.x
		val minY = min.y
		val minZ = min.z
		val width = max.x - minX + 1
		val length = max.z - minZ + 1
		boundsArray = arrayOfNulls(width)
		for (key in blocks) {
			val x = blockKeyX(key) - minX
			val y = blockKeyY(key) - minY
			val z = blockKeyZ(key) - minZ
			var yBoundsArray = boundsArray[x]
			if (yBoundsArray == null) {
				yBoundsArray = arrayOfNulls(length)
				boundsArray[x] = yBoundsArray
			}
			var yBounds = yBoundsArray[z]
			if (yBounds == null) {
				yBounds = IntArray(2)
				yBounds[0] = y
				yBounds[1] = y
				boundsArray[x]!![z] = yBounds
			} else if (y < yBounds[0]) {
				yBounds[0] = y
			} else if (y > yBounds[1]) {
				yBounds[1] = y
			}
		}
	}

	fun calculateMinMax(blocks: Set<Long>) {
		if (blocks.isEmpty()) {
			boundsArray = arrayOfNulls(0)
			return
		}
		val start = blocks.iterator().next()
		var minX = blockKeyX(start)
		var minY = blockKeyY(start)
		var minZ = blockKeyZ(start)
		var maxX = minX
		var maxY = minY
		var maxZ = minZ
		for (key in blocks) {
			val x = blockKeyX(key)
			val y = blockKeyY(key)
			val z = blockKeyZ(key)
			if (x < minX) minX = x
			if (x > maxX) maxX = x
			if (y < minY) minY = y
			if (y > maxY) maxY = y
			if (z < minZ) minZ = z
			if (z > maxZ) maxZ = z
		}
		min = Vec3i(minX, minY, minZ)
		max = Vec3i(maxX, maxY, maxZ)
	}

	fun contains(x: Int, y: Int, z: Int, minX: Int, minY: Int, minZ: Int, tolerance: Int): Boolean {
		val zArray = boundsArray[x - minX] ?: return false
		val yBounds = zArray[z - minZ] ?: return false
		val yDiff = y - minY
		return yDiff >= yBounds[0] - tolerance && yDiff <= yBounds[1] + tolerance
	}
}