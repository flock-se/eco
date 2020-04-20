package community.flock.eco.iso.country

import community.flock.eco.iso.country.resolvers.CountryIsoQueryResolver
import community.flock.eco.iso.country.services.CountryIsoService
import community.flock.eco.iso.country.controllers.CountryIsoController
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(CountryIsoService::class,
        CountryIsoController::class,
        CountryIsoQueryResolver::class
)
class CountryIsoConfiguration