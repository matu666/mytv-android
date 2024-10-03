package top.yogiczy.mytv.core.data.entities.epg

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme.Companion.isLive
import java.util.Calendar

/**
 * 频道节目单
 */
@Serializable
data class Epg(
    /**
     * 频道名称
     */
    val channelList: List<String> = emptyList(),

    /**
     * 图标
     */
    val logo: String? = null,

    /**
     * 节目列表
     */
    val programmeList: EpgProgrammeList = EpgProgrammeList(),
) {
    companion object {
        fun Epg.recentProgramme(): EpgProgrammeRecent {
            val liveProgramIndex = programmeList.indexOfFirst { it.isLive() }

            return if (liveProgramIndex != -1) {
                val now = programmeList[liveProgramIndex]
                val next = programmeList.getOrNull(liveProgramIndex + 1)

                EpgProgrammeRecent(now = now, next = next)
            } else {
                EpgProgrammeRecent(now = null, next = null)
            }
        }

        fun example(channel: Channel): Epg {
            return Epg(
                channelList = listOf(channel.epgName),
                programmeList = EpgProgrammeList(
                    List(100) { index ->
                        val startAt =
                            System.currentTimeMillis() - 3500 * 1000 * 24 * 2 + index * 3600 * 1000
                        EpgProgramme(
                            title = "${channel.epgName}节目${index + 1}",
                            startAt = startAt,
                            endAt = startAt + 3600 * 1000
                        )
                    }
                )
            )
        }

        fun empty(channel: Channel): Epg {
            val dayStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val dayEnd = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }

            return Epg(
                channelList = listOf(channel.epgName),
                programmeList = EpgProgrammeList(
                    listOf(
                        EpgProgramme(
                            title = "暂无节目",
                            startAt = dayStart.timeInMillis,
                            endAt = dayEnd.timeInMillis,
                        )
                    )
                )
            )
        }
    }
}