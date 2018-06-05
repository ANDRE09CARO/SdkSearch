package com.jakewharton.sdksearch.options.ui

import com.jakewharton.sdksearch.options.presenter.OptionsPresenter.Event
import com.jakewharton.sdksearch.options.presenter.OptionsPresenter.Model
import com.jakewharton.sdksearch.store.Config
import kotlinx.coroutines.experimental.channels.SendChannel
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.NonElementParentNode

class OptionsViewBinder(
  parent: NonElementParentNode,
  private val events: SendChannel<Event>
) {
  private val itemCount = parent.getElementById("item-count") as HTMLSpanElement

  private val gitWebInput = parent.getElementById("git-web") as HTMLInputElement
  private val dacInput = parent.getElementById("dac") as HTMLInputElement

  private val defaultsButton = parent.getElementById("defaults") as HTMLButtonElement
  private val saveButton = parent.getElementById("save") as HTMLButtonElement

  init {
    saveButton.onclick = {
      val config = Config(gitWebInput.value, dacInput.value)
      events.offer(Event.Save(config))
    }

    defaultsButton.onclick = {
      events.offer(Event.RestoreDefaults)
    }

    // TODO validation of form values.
  }

  fun bind(model: Model, oldModel: Model?) {
    itemCount.textContent = model.itemCount.toString()

    val config = model.config
    if (config != null) {
      gitWebInput.value = config.gitWebUrl
      dacInput.value = config.dacUrl
    }

    val disableControls = config == null || model.disableUpdates
    gitWebInput.disabled = disableControls
    dacInput.disabled = disableControls
    saveButton.disabled = disableControls
    defaultsButton.disabled = disableControls
  }
}
