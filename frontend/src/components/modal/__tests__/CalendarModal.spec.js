// tests/unit/CalendarModal.spec.js
import { mount } from "@vue/test-utils"
import CalendarModal from "../CalendarModal.vue"

describe("CalendarModal.vue", () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(CalendarModal)
  })

  afterEach(() => {
    wrapper.unmount()
  })

  it("컴포넌트가 올바르게 렌더링되는지 확인", () => {
    expect(wrapper.exists()).toBe(true)
    expect(wrapper.find(".calendar-modal").exists()).toBe(true)
  })

  it("현재 연도와 월이 올바르게 표시되는지 확인", () => {
    const currentDate = new Date()
    const expectedHeader = `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월`
    expect(wrapper.find(".calendar-header h2").text()).toBe(expectedHeader)
  })

  it("이전 달 버튼 클릭 시 월이 감소하는지 확인", async () => {
    const prevButton = wrapper.find("#prev-month")
    const initialMonth = wrapper.vm.currentMonth

    await prevButton.trigger("click")
    expect(wrapper.vm.currentMonth).toBe(initialMonth === 0 ? 11 : initialMonth - 1)
  })

  it("다음 달 버튼 클릭 시 월이 증가하는지 확인", async () => {
    const nextButton = wrapper.find("#next-month")
    const initialMonth = wrapper.vm.currentMonth

    await nextButton.trigger("click")
    expect(wrapper.vm.currentMonth).toBe(initialMonth === 11 ? 0 : initialMonth + 1)
  })

  it("닫기 버튼 클릭 시 이벤트가 발생하는지 확인", async () => {
    const closeButton = wrapper.find("#close-btn")
    await closeButton.trigger("click")

    expect(wrapper.emitted()).toHaveProperty("closeCalendar")
  })

  it("캘린더에 날짜가 올바르게 렌더링되는지 확인", () => {
    const daysContainer = wrapper.find(".days")
    expect(daysContainer.exists()).toBe(true)
    expect(daysContainer.element.children.length).toBeGreaterThan(0) // 날짜가 렌더링되었는지 확인
  })
})
