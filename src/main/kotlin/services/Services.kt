package services

class MissingServiceException(serviceName: String) : Exception("Cannot find $serviceName in list of services!")


abstract class Service{
    // Heavily reliant on list to keep code cleaner
    abstract val routes: List<String>

    abstract fun getUserRoutes(id: Int):List<String>

}

class Strava:Service(){

    override val routes: List<String> = listOf("SRT", "CVT", "Perkiomen")

    override fun getUserRoutes(id: Int): List<String> {
        return routes.map { "$id$it" }
    }

}

class Rwgps:Service(){

    override val routes: List<String> = listOf("CVT", "Perkiomen", "Welsh Mountain")

    override fun getUserRoutes(id: Int): List<String> {
        return routes.map { "$it$id" }
    }

}

class Komoot:Service(){
    override val routes: List<String> = listOf("SRT", "Welsh Mountain", "Oaks to Philly")

    override fun getUserRoutes(id: Int): List<String> {
        return routes.map { "$id$it$id" }
    }

}

class RouteAggregates(val services: List<Service> = listOf(Strava(), Rwgps(), Komoot())){
    // this allows us to pass in an arbitrary number of services
    private val serviceMap = services.associate { it -> it::class.simpleName?.toLowerCase() to it }

    // Realized that since they're fixed
    // they don't have to be calculated on the fly
    val allRoutes = services.map{it.routes}.flatten()
    val uniqueRoutes = allRoutes.distinct()

    fun getAllUserRoutes(id: Int):List<String>{
        return services.map{it.getUserRoutes(id)}.flatten()
    }

    fun getUserRoutesByService(id: Int, services: List<String>):List<String>{
        return services.map{
            // legitimately not sure if this was best/a good way to handle this
            // But I would rather not let invalid values pass silently in this case
            try {
                serviceMap[it.toLowerCase()]!!.getUserRoutes(id)
            }catch (npe: NullPointerException){
                throw MissingServiceException(it)
            }

        }.flatten()
    }



}