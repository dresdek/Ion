package net.starlegacy.util

import com.google.gson.Gson
import com.sk89q.jnbt.CompoundTag
import net.starlegacy.util.SignUtils.SignNBTLine
import net.starlegacy.util.SignUtils
import org.bukkit.ChatColor
import java.lang.StringBuilder

object SignUtils {
	private val gson = Gson()
	fun convertLine(jsonLine: String?): String {
		if (jsonLine == null) return ""
		val line = gson.fromJson(jsonLine, SignNBTLine::class.java)
		val text = StringBuilder(line.text)
		if (line.extra != null) {
			for (signNBTLine in line.extra!!) text.append(signNBTLine.text)
		}
		return text.toString()
	}

	fun fromCompoundTag(nbt: CompoundTag?): Array<String> {
		val lines = arrayOf("", "", "", "")
		if (nbt == null) {
			return lines
		}
		for (i in 0..3) {
			lines[i] = convertLine(nbt.getString("Text" + (i + 1)))
		}
		return lines
	}

	class SignNBTLine {
		var extra: List<SignNBTLine>? = null
		private var color: String? = null
		var text: String? = null
			get() = if (color == null) field else ChatColor.valueOf(color!!.uppercase()).toString() + field

		fun setColor(color: String?) {
			this.color = color
		}
	}
}