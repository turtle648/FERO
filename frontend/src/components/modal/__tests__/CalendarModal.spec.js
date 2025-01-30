import { mount } from "@vue/test-utils" // Vue 컴포넌트를 마운트하고 테스트하기 위한 유틸리티
import CalendarModal from "../CalendarModal.vue" // 테스트 대상

describe("CalendarModal.vue", () => {
  let wrapper

  // 각 테스트 전에 컴포넌트를 새로 마운트하여 독립적인 테스트 환경을 보장
  beforeEach(() => {
    wrapper = mount(CalendarModal)
  })

  // 각 테스트 후에 컴포넌트를 언마운트하여 메모리 누수를 방지
  afterEach(() => {
    wrapper.unmount()
  })

  it("컴포넌트가 올바르게 렌더링되는지 확인", () => {
    // 컴포넌트가 정상적으로 마운트되었는지 확인 exists()는 컴포넌트가 존재하는지 확인하는 메서드
    expect(wrapper.exists()).toBe(true)
    expect(wrapper.find(".calendar-modal").exists()).toBe(true)
  })

  it("현재 연도와 월이 올바르게 표시되는지 확인", () => {
    const currentDate = new Date() // 현재 날짜를 가져옴
    const expectedHeader = `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월` // 예상되는 헤더 텍스트

    // 캘린더 헤더에 표시된 연도와 월이 현재 날짜와 일치하는지 확인
    // .text()는 DOM 요소의 텍스트 내용을 가져오는 메서드
    expect(wrapper.find(".calendar-header h2").text()).toBe(expectedHeader)
  })

  it("이전 달 버튼 클릭 시 월이 감소하는지 확인", async () => {
    // 이전 달 버튼을 선택
    const prevButton = wrapper.find("#prev-month")

    // 현재 월 값을 가져옴 (Vue의 반응형 데이터)
    const initialMonth = wrapper.vm.currentMonth

    // 이전 달 버튼 클릭 이벤트를 트리거
    await prevButton.trigger("click")
    // currentMonth가 올바르게 감소했는지 확인
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
    await closeButton.trigger("click") // 닫기 버튼 클릭 이벤트를 트리거

    // closeCalendar 이벤트가 발생했는지 확인
    // emitted()는 컴포넌트에서 발생한 이벤트를 추적하는 Jest 유틸리티 메서드
    expect(wrapper.emitted()).toHaveProperty("closeCalendar")
  })

  it("캘린더에 날짜가 올바르게 렌더링되는지 확인", () => {
    const daysContainer = wrapper.find(".days")

    // .days 컨테이너가 존재하는지 확인
    expect(daysContainer.exists()).toBe(true)

    // .days 컨테이너 안에 날짜가 하나 이상 렌더링되었는지 확인
    // toBeGreaterThan(0)은 자식 요소의 개수가 최소한 하나 이상인지 검증하기 위해 사용됨.
    expect(daysContainer.element.children.length).toBeGreaterThan(0) // 날짜가 렌더링되었는지 확인
  })
})
