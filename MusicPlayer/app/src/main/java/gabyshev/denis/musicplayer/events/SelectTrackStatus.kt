package gabyshev.denis.musicplayer.events

/**
 * Created by 1 on 19.07.2017.
 */
data class SelectTrackStatus(val action: EnumSelectStatus)

enum class EnumSelectStatus {
    CANCEL,
    ADD
}