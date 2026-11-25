package cz.utb.meteodenik.data.local

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Prevodniky {
    @TypeConverter fun casNaText(cas: LocalDateTime?): String? = cas?.toString()
    @TypeConverter fun textNaCas(text: String?): LocalDateTime? = text?.let(LocalDateTime::parse)
}