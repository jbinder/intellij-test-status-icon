package tsi

import com.intellij.openapi.components.ServiceManager

class TestStateService {

  private var state = TestState.Unknown
  private var previousState : TestState? = null

  fun getInstance(): TestStateService? {
    return ServiceManager.getService(TestStateService::class.java)
  }

  fun set(state: TestState) {
    this.previousState = this.state
    this.state = state
  }

  fun get(): TestState {
    return state
  }

  fun hasChanged(): Boolean {
    return state != previousState
  }

}