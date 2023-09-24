package com.gayses.tests

import com.gayses.api.data.model.Work
import com.gayses.api.data.model.WorkQueueItem
import com.gayses.api.data.repository.*
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.ResourceNotFoundException
import com.gayses.api.service.work.WorkService
import com.gayses.api.service.work.WorkServiceImpl
import com.gayses.tests.data.TestsDataStore
import com.gayses.tests.ext.streamOf
import com.gayses.tests.store.*
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

class WorkServiceTests {
    private val workRepository: WorkRepository = MockWorkRepository.create()
    private val unitRepository: UnitRepository = MockUnitRepository.create()
    private val workTypeRepository: WorkTypeRepository = MockWorkTypeRepository.create()
    private val workQueueRepository: WorkQueueRepository = MockWorkQueueRepository.create()
    private val performerRepository: PerformerRepository = MockPerformerRepository.create()

    private val workService: WorkService = WorkServiceImpl(
        workTypeRepository,
        unitRepository,
        performerRepository,
        workQueueRepository,
        workRepository
    )

    companion object {
        @JvmStatic
        fun provideValidWorkData(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 0, 0, TestsDataStore.workTypes.first().title, "Title", "ProductTitle", TestsDataStore.units.first().title, 23, TestsDataStore.performers.first().title, null, null, null, null, null, null),
                Arguments.of(1, 0, 0, "M23", "Title23", "ProductTitle23", "uu23", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 4, null, "M", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, 0, "M", "Title", "ProductTitle", null, 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, 0, "M", "Title", "ProductTitle", "uu", 1, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, 0, "M", "Title", "ProductTitle", "uu", Integer.MAX_VALUE, "Perf", null, null, null, null, null, null),
                // @formatter:on
            )

        @JvmStatic
        fun provideInvalidWorkData(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, -1, "M", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, Integer.MIN_VALUE, "M", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "ProductTitle", "", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "ProductTitle", "uu", -23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "ProductTitle", "uu", 0, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "ProductTitle", "uu", Integer.MIN_VALUE, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 0, "M", "Title", "ProductTitle", "uu", 23, "", null, null, null, null, null, null),
                // @formatter:on
            )

        @JvmStatic
        fun provideValidItemOrderData(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 0, 0),
                Arguments.of(1, 2, 2),
                Arguments.of(1, 4, 4),
                Arguments.of(1, 4, null),
                Arguments.of(1, 4, Integer.MAX_VALUE),
                Arguments.of(3, 0, 0),
                Arguments.of(3, 0, null),
                Arguments.of(3, 0, Integer.MAX_VALUE),
                // @formatter:on
            )

        @JvmStatic
        fun provideValidWorkIds(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 1),
                Arguments.of(2, 2),
                Arguments.of(1, 3),
                Arguments.of(2, 4),
                Arguments.of(1, 10),
                Arguments.of(2, 11),
                Arguments.of(1, 12),
                Arguments.of(2, 13),
                // @formatter:on
            )

        @JvmStatic
        fun provideInvalidWorkIds(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 100),
                Arguments.of(1, 0),
                Arguments.of(1, -1),
                Arguments.of(1, Integer.MAX_VALUE),
                Arguments.of(1, Integer.MIN_VALUE),
                // @formatter:on
            )

        @JvmStatic
        fun provideValidWorkDataToUpdate(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 1, TestsDataStore.workTypes.first().title, "Title", "ProductTitle", TestsDataStore.units.first().title, 23, TestsDataStore.performers.first().title, null, null, null, null, null, null),
                Arguments.of(1, 3, "M23", "Title23", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M23", "Title23", "ProductTitle23", "uu23", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", null, 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", 1, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", Integer.MAX_VALUE, "Perf", null, null, null, null, null, null),
                // @formatter:on
            )

        @JvmStatic
        fun provideInvalidWorkDataToUpdate(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 1, "", "Title", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "", "ProductTitle", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "", "uu", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "", 23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", 0, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", -23, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", Integer.MIN_VALUE, "Perf", null, null, null, null, null, null),
                Arguments.of(1, 1, "M", "Title", "ProductTitle", "uu", 23, "", null, null, null, null, null, null),
                // @formatter:on
            )

        @JvmStatic
        fun provideValidItemOrderDataToUpdate(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 1, 0, 0),
                Arguments.of(1, 1, 1, 1),
                Arguments.of(1, 1, 3, 3),
                Arguments.of(1, 1, 3, 4),
                Arguments.of(1, 1, 3, 5),
                Arguments.of(1, 1, 3, 999),
                Arguments.of(1, 1, 3, Integer.MAX_VALUE),

                Arguments.of(1, 3, 0, 0),
                Arguments.of(1, 3, 1, 1),
                Arguments.of(1, 3, 3, 3),
                Arguments.of(1, 3, 3, 4),
                Arguments.of(1, 3, 3, 5),
                Arguments.of(1, 3, 3, 999),
                Arguments.of(1, 3, 3, Integer.MAX_VALUE),
                // @formatter:on
            )

        @JvmStatic
        fun provideInvalidItemOrderDataToUpdate(): Stream<Arguments> =
            streamOf(
                // @formatter:off
                Arguments.of(1, 1, -1),
                Arguments.of(1, 1, Integer.MIN_VALUE),
                Arguments.of(1, 1, Integer.MIN_VALUE),
                // @formatter:on
            )
    }

    @Nested
    @DisplayName("Create work")
    inner class CreateWorkMethod {
        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideValidWorkData")
        fun whenGivenValidData_thenReturnsCreatedWorkQueueItem(
            projectId: Long,
            expectedItemOrder: Int,
            itemOrder: Int?,
            type: String,
            workTitle: String,
            productTitle: String,
            unit: String?,
            amount: Int,
            performer: String,
            expectedPaymentDate: Date?,
            paymentDate: Date?,
            expectedDeliveryDate: Date?,
            deliveryDate: Date?,
            expectedFinishDate: Date?,
            finishDate: Date?
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val result = workService.createWork(
                project,
                itemOrder,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )

            // assert result
            Assertions.assertEquals(expectedItemOrder, result.order)
            Assertions.assertEquals(project, result.project)

            assertWorkContent(
                result.work,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )

            val workCaptor = slot<Work>()
            val workQueueItemCaptor = slot<WorkQueueItem>()

            verify { workRepository.save(capture(workCaptor)) }
            verify { workQueueRepository.save(capture(workQueueItemCaptor)) }

            // assert captured
            Assertions.assertEquals(expectedItemOrder, workQueueItemCaptor.captured.order)
            Assertions.assertEquals(project, workQueueItemCaptor.captured.project)

            assertWorkContent(
                workCaptor.captured,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideInvalidWorkData")
        fun whenGivenInvalidData_thanThrowsException(
            projectId: Long,
            itemOrder: Int?,
            type: String,
            workTitle: String,
            productTitle: String,
            unit: String?,
            amount: Int,
            performer: String,
            expectedPaymentDate: Date?,
            paymentDate: Date?,
            expectedDeliveryDate: Date?,
            deliveryDate: Date?,
            expectedFinishDate: Date?,
            finishDate: Date?
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                workService.createWork(
                    project,
                    itemOrder,
                    type,
                    workTitle,
                    productTitle,
                    unit,
                    amount,
                    performer,
                    expectedPaymentDate,
                    paymentDate,
                    expectedDeliveryDate,
                    deliveryDate,
                    expectedFinishDate,
                    finishDate
                )
            }
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideValidItemOrderData")
        fun whenGivenValidOrder_thenItemCreatedAtTheCorrectOrder(
            projectId: Long,
            expectedItemOrder: Int,
            itemOrder: Int?
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val result = workService.createWork(
                project,
                itemOrder,
                "M",
                "Title",
                "ProductTitle",
                "uu",
                23,
                "Performer",
                null,
                null,
                null,
                null,
                null,
                null
            )

            // assert result
            Assertions.assertEquals(expectedItemOrder, result.order)
            Assertions.assertEquals(project, result.project)

            val workCaptor = slot<Work>()
            val workQueueItemCaptor = slot<WorkQueueItem>()

            verify { workRepository.save(capture(workCaptor)) }
            verify { workQueueRepository.save(capture(workQueueItemCaptor)) }

            // assert captured
            Assertions.assertEquals(expectedItemOrder, workQueueItemCaptor.captured.order)
            Assertions.assertEquals(project, workQueueItemCaptor.captured.project)
        }
    }

    @Nested
    @DisplayName("Get work")
    inner class GetWorkMethod {
        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideValidWorkIds")
        fun whenGivenValidWorkId_thenReturnsWorkItem(projectId: Long, workId: Long) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val result = workService.getWork(project, workId)

            val captor = slot<Long>()
            verify { workRepository.findBy_idAndQueueItem_Project_Id(capture(captor), projectId) }

            Assertions.assertEquals(workId, result.id)
            Assertions.assertEquals(workId, captor.captured)
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideInvalidWorkIds")
        fun whenGivenInvalidWorkId_thenReturnsWorkItem(projectId: Long, workId: Long) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                workService.getWork(project, workId)
            }
        }
    }

    @Nested
    @DisplayName("Update work queue")
    inner class UpdateWorkMethod {
        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideValidWorkDataToUpdate")
        fun whenGivenValidDataWithValidId_thenReturnsUpdatedItem(
            projectId: Long,
            workId: Long,
            type: String,
            workTitle: String,
            productTitle: String,
            unit: String?,
            amount: Int,
            performer: String,
            expectedPaymentDate: Date?,
            paymentDate: Date?,
            expectedDeliveryDate: Date?,
            deliveryDate: Date?,
            expectedFinishDate: Date?,
            finishDate: Date?
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val result = workService.updateWork(
                project,
                workId,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )

            // assert result
            assertWorkContent(
                result,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )

            val workCaptor = slot<Work>()
            verify { workRepository.save(capture(workCaptor)) }

            // assert captured
            assertWorkContent(
                workCaptor.captured,
                type,
                workTitle,
                productTitle,
                unit,
                amount,
                performer,
                expectedPaymentDate,
                paymentDate,
                expectedDeliveryDate,
                deliveryDate,
                expectedFinishDate,
                finishDate
            )
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideInvalidWorkDataToUpdate")
        fun whenGivenInvalidDataWithValidId_thanThrowsException(
            projectId: Long,
            workId: Long,
            type: String,
            workTitle: String,
            productTitle: String,
            unit: String?,
            amount: Int,
            performer: String,
            expectedPaymentDate: Date?,
            paymentDate: Date?,
            expectedDeliveryDate: Date?,
            deliveryDate: Date?,
            expectedFinishDate: Date?,
            finishDate: Date?
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                workService.updateWork(
                    project,
                    workId,
                    type,
                    workTitle,
                    productTitle,
                    unit,
                    amount,
                    performer,
                    expectedPaymentDate,
                    paymentDate,
                    expectedDeliveryDate,
                    deliveryDate,
                    expectedFinishDate,
                    finishDate
                )
            }
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideInvalidWorkIds")
        fun whenGivenInvalidId_thanThrowsException(
            projectId: Long,
            workId: Long
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                workService.updateWork(
                    project,
                    workId,
                    "M",
                    "Title",
                    "ProductTitle",
                    "uu",
                    1,
                    "Perf",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }

    @Nested
    @DisplayName("Reorder work queue")
    inner class ReorderWorkMethod {
        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideValidItemOrderDataToUpdate")
        fun whenGivenValidData_thenSuccessfullyReordered(
            projectId: Long,
            workId: Long,
            expectedItemOrder: Int,
            newItemOrder: Int
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val work = TestsDataStore.works.first {
                it.id == workId
            }

            val result = workService.reorderWorkOrder(project, work, newItemOrder)

            Assertions.assertEquals(expectedItemOrder, result.order)
        }

        @ParameterizedTest
        @MethodSource("com.gayses.tests.WorkServiceTests#provideInvalidItemOrderDataToUpdate")
        fun whenGivenInvalidData_thenSuccessfullyReordered(
            projectId: Long,
            workId: Long,
            newItemOrder: Int
        ) {
            val project = TestsDataStore.projects.first {
                it.id == projectId
            }

            val work = TestsDataStore.works.first {
                it.id == workId
            }

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                workService.reorderWorkOrder(project, work, newItemOrder)
            }
        }
    }


    private fun assertWorkContent(
        work: Work,
        type: String,
        workTitle: String,
        productTitle: String,
        unit: String?,
        amount: Int,
        performer: String,
        expectedPaymentDate: Date?,
        paymentDate: Date?,
        expectedDeliveryDate: Date?,
        deliveryDate: Date?,
        expectedFinishDate: Date?,
        finishDate: Date?
    ) {
        Assertions.assertEquals(type, work.type.title)
        Assertions.assertEquals(workTitle, work.workTitle)
        Assertions.assertEquals(productTitle, work.productTitle)
        Assertions.assertEquals(unit, work.unit?.title)
        Assertions.assertEquals(amount, work.amount)
        Assertions.assertEquals(performer, work.performer.title)
        Assertions.assertEquals(expectedPaymentDate, work.expectedPaymentDate)
        Assertions.assertEquals(paymentDate, work.paymentDate)
        Assertions.assertEquals(expectedDeliveryDate, work.expectedDeliveryDate)
        Assertions.assertEquals(deliveryDate, work.deliveryDate)
        Assertions.assertEquals(expectedFinishDate, work.expectedFinishDate)
        Assertions.assertEquals(finishDate, work.finishDate)
    }
}