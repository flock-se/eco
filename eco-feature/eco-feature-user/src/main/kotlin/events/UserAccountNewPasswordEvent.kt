package community.flock.eco.feature.user.events

import community.flock.eco.feature.user.model.UserAccountPassword

class UserAccountNewPasswordEvent(override val entity: UserAccountPassword) : UserAccountEvent(entity)
