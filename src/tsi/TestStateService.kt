package tsi

import com.intellij.openapi.components.ServiceManager

class TestStateService {
  private var state = TestState.Unknown

  fun getInstance(): TestStateService? {
    return ServiceManager.getService(TestStateService::class.java)
  }

  fun set(state: TestState) {
    this.state = state
  }

  fun get(): TestState {
    return state
  }
}