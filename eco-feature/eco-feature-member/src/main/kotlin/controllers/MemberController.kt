package community.flock.eco.feature.member.controllers

import MemberGraphqlMapper
import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.events.MemberEvent
import community.flock.eco.feature.member.graphql.MemberInput
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.services.MemberService
import community.flock.eco.feature.member.specifications.MemberSpecification
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import community.flock.eco.feature.member.graphql.Member as MemberGraphql

@RestController
@RequestMapping("/api/members")
class MemberController(
        private val memberGraphqlMapper: MemberGraphqlMapper,
        private val memberService: MemberService
) {

    data class MergeForm(val mergeMemberIds: List<Long>, val newMember: MemberInput)

    @GetMapping
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findAll(
            @RequestParam search: String?,
            @RequestParam statuses: Set<MemberStatus>?,
            @RequestParam groups: Set<String>?,
            page: Pageable): ResponseEntity<List<MemberGraphql>> {

        val specification = MemberSpecification(
                search = search ?: "",
                statuses = statuses ?: setOf(
                        MemberStatus.NEW,
                        MemberStatus.ACTIVE,
                        MemberStatus.DISABLED),
                groups = groups ?: setOf()
        )
        return memberService.findAll(specification, page)
                .map { it.produce() }
                .toResponse()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findById(
            @PathVariable("id") id: Long) = memberService
            .findById(id)
            ?.produce()
            .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun create(@RequestBody form: MemberInput) = memberService
            .create(form.consume())
            .produce()
            .toResponse()

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun update(@PathVariable("id") id: String, @RequestBody member: MemberInput) = memberService
            .update(id.toLong(), member.consume())
            .produce()
            .toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun delete(@PathVariable("id") id: String) = memberService
            .delete(id.toLong())
            .produce()
            .toResponse()

    @PostMapping("/merge")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun merge(@RequestBody form: MergeForm) = memberService
            .merge(form.mergeMemberIds, form.newMember.consume())
            .produce()
            .toResponse()

    private fun Member.produce() = memberGraphqlMapper.produce(this)
    private fun MemberInput.consume(member: Member? = null) = memberGraphqlMapper
            .consume(this)

}
