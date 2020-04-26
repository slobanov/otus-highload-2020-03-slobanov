package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.Following
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.repository.FollowerRepository
import ru.amai.highload.social.service.FollowerService
import ru.amai.highload.social.service.UserService

@Service
class FollowerServiceImpl(
    private val followerRepository: FollowerRepository,
    private val userService: UserService
) : FollowerService {

    override fun listFollowedBy(login: String): List<String> {
        val followedUserIds = userService.withUser(login) {
            followerRepository.findFollowedBy(id!!)
                .map(Following::followedId)
        } ?: emptyList()

        return userService.listUsersByIds(followedUserIds.filterNotNull())
            .map(User::login)
    }

    override fun canFollow(currentLogin: String, userToFollowLogin: String): Boolean {
        val tryingToFollowSelf = currentLogin == userToFollowLogin
        return if (tryingToFollowSelf) false
        else {
            userService.withUser(currentLogin) {
                val followerId = id!!
                userService.withUser(userToFollowLogin) {
                    followerRepository.findByFollowerAndFollowerId(
                        followedId = id!!,
                        followerId = followerId
                    )
                }
            } == null
        }
    }

    override fun follow(currentLogin: String, userToFollowLogin: String): Following {
        require(currentLogin != userToFollowLogin)

        val following = userService.withUser(currentLogin) {
            val followerId = id!!
            userService.withUser(userToFollowLogin) {
                followerRepository.save(
                    Following(
                        followedId = id!!,
                        followerId = followerId
                    )
                )
            }
        }
        requireNotNull(following)
        return following
    }
}
