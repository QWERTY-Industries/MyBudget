package qi.mybudget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val cid: Int,
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "category_name") val categoryName: String
)