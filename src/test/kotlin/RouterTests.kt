import org.junit.Test
import services.*
import kotlin.test.assertEquals
import kotlin.test.fail


class ServiceTests {

    private val stravaService = Strava
    private val rwgpsService = Rwgps
    private val komootService = Komoot

    @Test
    fun `Strava service initialized with correct routes`() {
        assertEquals(
                listOf("SRT", "CVT", "Perkiomen"),
                stravaService.routes)
    }

    @Test
    fun `RWGPS service initialized with correct routes`() {
        assertEquals(
                listOf("CVT", "Perkiomen", "Welsh Mountain"),
                rwgpsService.routes)
    }

    @Test
    fun `Komoot service initialized with correct routes`() {
        assertEquals(
                listOf("SRT", "Welsh Mountain", "Oaks to Philly"),
                komootService.routes)
    }

    @Test
    fun `Strava getUserRoutes returns correct values`() {
        assertEquals(
                listOf("42SRT", "42CVT", "42Perkiomen"),
                stravaService.getUserRoutes(42))
    }

    @Test
    fun `RWGPS getUserRoutes returns correct values`() {
        assertEquals(
                listOf("CVT42", "Perkiomen42", "Welsh Mountain42"),
                rwgpsService.getUserRoutes(42))
    }

    @Test
    fun `Komoot getUserRoutes return correct values`() {
        assertEquals(
                listOf("42SRT42", "42Welsh Mountain42", "42Oaks to Philly42"),
                komootService.getUserRoutes(42))
    }
}

class RouteAggregatesTests{
    private val routeAggregates = RouteAggregates()

    @Test
    fun `Route aggregates allRoutes initialized with correct routes`(){
        assertEquals(
                listOf("SRT", "CVT", "Perkiomen", "CVT", "Perkiomen",
                        "Welsh Mountain", "SRT", "Welsh Mountain", "Oaks to Philly"),
                routeAggregates.allRoutes
        )
    }

    @Test
    fun `Route aggregates uniqueRoutes initialized with correct routes`(){
        assertEquals(
                listOf("SRT", "CVT", "Perkiomen", "Welsh Mountain", "Oaks to Philly"),
                routeAggregates.uniqueRoutes
        )
    }

    @Test
    fun `Route aggregates getUserRoutes returns routes formatted correctly`(){
        assertEquals(listOf(
                "42SRT", "42CVT", "42Perkiomen",
                "CVT42", "Perkiomen42", "Welsh Mountain42",
                "42SRT42", "42Welsh Mountain42", "42Oaks to Philly42"),
                routeAggregates.getAllUserRoutes(42)
        )
    }

    @Test
    fun `Route aggregates getUserRoutesByService returns correct values`(){
        assertEquals(listOf(
                "42SRT42", "42Welsh Mountain42", "42Oaks to Philly42",
                "CVT42", "Perkiomen42", "Welsh Mountain42"),
                routeAggregates.getUserRoutesByService(42, listOf("Komoot","RWGPS"))
        )
    }

    @Test
    fun `Nonexistent service throws correct error`(){

        try {
            routeAggregates.getUserRoutesByService(42, listOf("NotARealService"))
            fail("Expected an IndexOutOfBoundsException to be thrown")
        } catch (mse: MissingServiceException) {
            assertEquals(mse.message, "Cannot find NotARealService in list of services!")
        }

    }

}