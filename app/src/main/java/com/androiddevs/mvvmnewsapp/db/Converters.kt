package id.alpine.beritamvvm.db

import androidx.room.TypeConverter
import id.alpine.beritamvvm.models.Source


class Converters {

    @TypeConverter
    fun fromSources(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name);
    }
}