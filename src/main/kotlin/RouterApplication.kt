import services.RouteAggregates

fun main(args: Array<String>) {

    // Printing out to match example output
    val routerAggregates = RouteAggregates()
    val serviceList = listOf("Komoot", "RWGPS")

    println("All Routes: ${routerAggregates.allRoutes}")
    println("All Unique Routes: ${routerAggregates.uniqueRoutes}")
    println("For user 42: ${routerAggregates.getAllUserRoutes(42)}")
    println("For user 42 services ${serviceList}: ${routerAggregates.getUserRoutesByService(42, serviceList)}")
}