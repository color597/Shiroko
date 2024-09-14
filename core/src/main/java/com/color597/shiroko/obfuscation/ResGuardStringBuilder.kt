package com.color597.shiroko.obfuscation

import com.color597.shiroko.utils.Utils
import java.lang.IllegalArgumentException
import java.util.HashSet
import java.util.regex.Pattern

/**
 * 混淆字典.
 *
 *
 * Copied from: https://github.com/shwenzhang/AndResGuard
 */
class ResGuardStringBuilder {
    private val mReplaceStringBuffer = arrayListOf<String>()
    private val mIsReplaced: MutableSet<Int?> = HashSet<Int?>()
    private val mIsWhiteList: MutableSet<Int?> = HashSet<Int?>()
    private val aToZ = (97..122).shuffled().union((65..90)).map { Char(it).toString() }.union(setOf("_")).shuffled()
    private val numbers = (0..9).shuffled().map { it.toString() }
    private val defaultDictionary = aToZ.union(numbers).shuffled()

    /**
     * 在window上面有些关键字是不能作为文件名的
     * CON, PRN, AUX, CLOCK$, NUL
     * COM1, COM2, COM3, COM4, COM5, COM6, COM7, COM8, COM9
     * LPT1, LPT2, LPT3, LPT4, LPT5, LPT6, LPT7, LPT8, and LPT9.
     */
    private val mFileNameBlackList = hashSetOf(
        "con", "prn", "aux", "nul"
    )

    fun reset(blacklistPatterns: HashSet<Pattern?>?) {
        mReplaceStringBuffer.clear()
        mIsReplaced.clear()
        mIsWhiteList.clear()

        aToZ.shuffled().forEach { first ->
            if (!Utils.match(first, blacklistPatterns)) {
                mReplaceStringBuffer.add(first)
            }
            defaultDictionary.shuffled().forEach { second ->
                val str1 = first + second
                if (!Utils.match(str1, blacklistPatterns)) {
                    mReplaceStringBuffer.add(str1)
                }
                defaultDictionary.shuffled().forEach { third ->
                    val str = str1 + third
                    if (!mFileNameBlackList.contains(str) && !Utils.match(str, blacklistPatterns)) {
                        mReplaceStringBuffer.add(str)
                    }
                }
            }
        }

        mReplaceStringBuffer.shuffle()
    }

    // 对于某种类型用过的mapping，全部不能再用了
    fun removeStrings(collection: MutableCollection<String?>?) {
        if (collection == null) return
        mReplaceStringBuffer.removeAll(collection)
    }

    fun isReplaced(id: Int): Boolean {
        return mIsReplaced.contains(id)
    }

    fun isInWhiteList(id: Int): Boolean {
        return mIsWhiteList.contains(id)
    }

    fun setInWhiteList(id: Int) {
        mIsWhiteList.add(id)
    }

    fun setInReplaceList(id: Int) {
        mIsReplaced.add(id)
    }

    @Throws(IllegalArgumentException::class)
    fun getReplaceString(names: MutableCollection<String>?): String {
        require(!mReplaceStringBuffer.isEmpty()) { "now can only obfuscation less than 35594 in a single type\n" }

        if (names != null) {
            for (i in mReplaceStringBuffer.indices) {
                val name = mReplaceStringBuffer[i]
                if (names.contains(name)) {
                    continue
                }
                return mReplaceStringBuffer.removeAt(i)
            }
        }
        return mReplaceStringBuffer.removeAt(0)
    }

    fun getReplaceString(): String? {
        return getReplaceString(null)
    }
}
