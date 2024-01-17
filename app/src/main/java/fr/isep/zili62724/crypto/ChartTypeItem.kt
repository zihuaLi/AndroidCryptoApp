package fr.isep.zili62724.crypto

data class ChartTypeItem(
    val title: String,
    val chartType: ChartType
)

enum class ChartType {
    PIE_CHART,
    GRID_DATA,
    // 可以添加其他图表类型
}
