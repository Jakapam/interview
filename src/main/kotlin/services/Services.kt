package services

class MissingServiceException(serviceName: String) : Exception("Cannot find $serviceName in list of services!")


sealed class Service{
    // Heavily reliant on list to keep code cleaner

    abstract val routes: List<String>
    abstract val transformer: (id: Int, name: String)-> String

    fun getUserRoutes(id: Int):List<String>{
        return routes.map{ transformer(id, it) }
    }

}

object Strava:Service(){

    override val routes: List<String> = listOf("SRT", "CVT", "Perkiomen")
    override val transformer = {id: Int, name: String -> "$id$name"}

}

object Rwgps:Service(){

    override val routes: List<String> = listOf("CVT", "Perkiomen", "Welsh Mountain")
    override val transformer = {id: Int, name: String -> "$name$id"}

}

object Komoot:Service(){

    override val routes: List<String> = listOf("SRT", "Welsh Mountain", "Oaks to Philly")
    override val transformer = {id: Int, name: String -> "$id$name$id"}

}


class RouteAggregates{

    val services = Service::class.sealedSubclasses.mapNotNull { it.objectInstance }
    val serviceMap = services.associate { it::class.simpleName?.toLowerCase() to it }

    // Realized that since they're fixed
    // they don't have to be calculated on the fly
    val allRoutes = services.map{it.routes}.flatten()
    val uniqueRoutes = allRoutes.distinct()

    fun getAllUserRoutes(id: Int):List<String>{
        return services.map{it.getUserRoutes(id)}.flatten()
    }

    fun getUserRoutesByService(id: Int, services: List<String>):List<String>{
        return services.map{

            val service = serviceMap[it.toLowerCase()] ?: throw MissingServiceException(it)
            service.getUserRoutes(id)
        }.flatten()
    }



}