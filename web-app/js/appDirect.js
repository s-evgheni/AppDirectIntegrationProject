window.scrollTo(0, 1), $(function () {
    navigator.userAgent.match(/(iPad|iPhone|iPod touch);.*CPU.*OS 7_\d/i) && $("html").addClass("ios7")
}), Test.FieldLabels = Test.Base.extend({inputFields: null, selectFields: null, init: function () {
    this.inputFields = "input[placeholder], textarea[placeholder]", this.selectFields = "select.placeholder", this.setupEvents()
}, setupEvents: function () {
    this.addEvent(this.inputFields, "keyup, test-field-label", null, "onChange", !0), this.addEvent(this.selectFields, "change, test-field-label", null, "onChange", !0), this.addEvent(this.inputFields, "focus, blur", null, "toggleWrapperFocus", !0), this.addEvent(this.selectFields, "focus, blur", null, "toggleWrapperFocus", !0), $(this.inputFields).trigger("test-field-label"), $(this.selectFields).trigger("test-field-label")
}, onChange: function (event) {
    var $field = $(event.target), $wrapper = $field.closest(".input-wrapper");
    $field.val() && 0 === $wrapper.find(".placeholder-label").length ? this.showLabel($field, $wrapper) : $field.val() || this.hideLabel($field, $wrapper)
}, toggleWrapperFocus: function (event) {
    var $wrapper = $(event.target).closest(".input-wrapper");
    $wrapper.toggleClass("focus")
}, showLabel: function ($field, $wrapper) {
    var placeholder = null;
    placeholder = $field.is("select") ? $field.find('option[value=""]').text() : $field.attr("placeholder");
    var label = $('<div class="placeholder-label">').text(placeholder);
    $wrapper.append(label), label.hide().fadeIn(300)
}, hideLabel: function ($field, $wrapper) {
    $wrapper.find(".placeholder-label").fadeOut(300, function () {
        $(this).remove()
    })
}}), new Test.FieldLabels;