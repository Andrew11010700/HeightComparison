package ua.scootersoft.heightcomparison.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.utils.Constants

@Entity(tableName = Constants.PEOPLE_TABLE)
data class ComparedPerson(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var imageUrl: String? = null,
    @Ignore
    var gender: Gender = Gender.MAN,
    var name: String = "",
    var heightCm: Int = 170,
    var isShowPerson: Boolean = true,
    var defaultImage: Int = if (gender == Gender.MAN) R.drawable.default_man else R.drawable.default_woman,
    var sortIndex: Long = id
)