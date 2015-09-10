var Base = function () {
};
Base.extend = function (_instance, _static) {
    var extend = Base.prototype.extend;
    Base._prototyping = !0;
    var proto = new this;
    extend.call(proto, _instance), proto.base = function () {
    }, delete Base._prototyping;
    var constructor = proto.constructor, klass = proto.constructor = function () {
        if (!Base._prototyping)if (this._constructing || this.constructor == klass)this._constructing = !0, constructor.apply(this, arguments), delete this._constructing; else if (null != arguments[0])return(arguments[0].extend || extend).call(arguments[0], proto)
    };
    return klass.ancestor = this, klass.extend = this.extend, klass.forEach = this.forEach, klass.implement = this.implement, klass.prototype = proto, klass.toString = this.toString, klass.valueOf = function (type) {
        return"object" == type ? klass : constructor.valueOf()
    }, extend.call(klass, _static), "function" == typeof klass.init && klass.init(), klass
}, Base.prototype = {extend: function (source, value) {
    if (arguments.length > 1) {
        var ancestor = this[source];
        if (ancestor && "function" == typeof value && (!ancestor.valueOf || ancestor.valueOf() != value.valueOf()) && /\bbase\b/.test(value)) {
            var method = value.valueOf();
            value = function () {
                var previous = this.base || Base.prototype.base;
                this.base = ancestor;
                var returnValue = method.apply(this, arguments);
                return this.base = previous, returnValue
            }, value.valueOf = function (type) {
                return"object" == type ? value : method
            }, value.toString = Base.toString
        }
        this[source] = value
    } else if (source) {
        var extend = Base.prototype.extend;
        Base._prototyping || "function" == typeof this || (extend = this.extend || extend);
        for (var proto = {toSource: null}, hidden = ["constructor", "toString", "valueOf"], i = Base._prototyping ? 0 : 1; key = hidden[i++];)source[key] != proto[key] && extend.call(this, key, source[key]);
        for (var key in source)proto[key] || extend.call(this, key, source[key])
    }
    return this
}}, Base = Base.extend({constructor: function () {
    this.extend(arguments[0])
}}, {ancestor: Object, version: "1.1", forEach: function (object, block, context) {
    for (var key in object)void 0 === this.prototype[key] && block.call(context, object[key], key, object)
}, implement: function () {
    for (var i = 0; i < arguments.length; i++)"function" == typeof arguments[i] ? arguments[i](this.prototype) : this.prototype.extend(arguments[i]);
    return this
}, toString: function () {
    return String(this.valueOf())
}}), Test = {$window: $(window), $document: $(document), $body: $(document.body), windowSnapPoints: {small: {min: 0, max: 640}, medium: {min: 641, max: 1024}, large: {min: 1025, max: 99999}}, isWindowValidSize: function (size) {
    switch (size) {
        case"small-only":
            if (Test.$window.width() >= Test.windowSnapPoints.small.min && Test.$window.width() <= Test.windowSnapPoints.small.max)return!0;
            break;
        case"small-up":
            return!0;
        case"medium-only":
            if (Test.$window.width() >= Test.windowSnapPoints.medium.min && Test.$window.width() < Test.windowSnapPoints.medium.max)return!0;
            break;
        case"medium-up":
            if (Test.$window.width() >= Test.windowSnapPoints.medium.min)return!0
    }
    return!1
}, log: function (msg) {
    "undefined" != typeof console && "function" == typeof console.log
}, isArray: function (value) {
    return value instanceof Array
}, getInputPostVal: function ($input) {
    var type = $input.attr("type"), val = $input.val();
    return"checkbox" == type || "radio" == type ? $input.prop("checked") ? val : null : Test.isArray(val) && "[]" != $input.attr("name").substr(-2) ? val.length ? val[val.length - 1] : null : val
}, findInputs: function (container) {
    return $(container).find("input,text,textarea,select,button")
}, getPostData: function (container) {
    for (var postData = {}, arrayInputCounters = {}, $inputs = Test.findInputs(container), i = 0; i < $inputs.length; i++) {
        var $input = $($inputs[i]);
        if (!$input.prop("disabled")) {
            var inputName = $input.attr("name");
            if (inputName) {
                var inputVal = Test.getInputPostVal($input);
                if (null !== inputVal) {
                    var isArrayInput = "[]" == inputName.substr(-2);
                    if (isArrayInput) {
                        var croppedName = inputName.substring(0, inputName.length - 2);
                        "undefined" == typeof arrayInputCounters[croppedName] && (arrayInputCounters[croppedName] = 0)
                    }
                    Test.isArray(inputVal) || (inputVal = [inputVal]);
                    for (var j = 0; j < inputVal.length; j++) {
                        if (isArrayInput) {
                            var inputName = croppedName + "[" + arrayInputCounters[croppedName] + "]";
                            arrayInputCounters[croppedName]++
                        }
                        postData[inputName] = inputVal[j]
                    }
                }
            }
        }
    }
    return postData
}, getUrlParameters: function () {
    var parameters = {}, parts = document.location.toString().split("?")[1];
    if ("undefined" != typeof parts) {
        parts = parts.split("&");
        for (var i = 0; i < parts.length; i++) {
            var part = parts[i].split("=");
            part[1] = decodeURIComponent(part[1].replace(/\+/g, " ")), part[0].search(/\[\]/) >= 0 ? (part[0] = part[0].replace("[]", ""), "object" != typeof parameters[part[0]] && (parameters[part[0]] = []), parameters[part[0]].push(part[1])) : parameters[part[0]] = part[1]
        }
    }
    return parameters
}, getUrlParameter: function (parameter) {
    var urlParameters = this.getUrlParameters();
    return"undefined" != typeof urlParameters[parameter] ? urlParameters[parameter] : null
}}, Test.Base = Base.extend({init: $.noop, settings: null, ajaxQueue: $({}), _namespace: null, _$events: null, constructor: function () {
    this._namespace = ".Test" + Math.floor(1e9 * Math.random()), this._$events = $(), this.init.apply(this, arguments)
}, setSettings: function (settings, defaults) {
    var baseSettings = "undefined" == typeof this.settings ? {} : this.settings;
    this.settings = $.extend(baseSettings, defaults, settings)
}, _formatEvents: function (events) {
    if ("string" == typeof events) {
        events = events.split(",");
        for (var i = 0; i < events.length; i++)events[i] = $.trim(events[i])
    }
    for (var i = 0; i < events.length; i++)events[i] += this._namespace;
    return events.join(" ")
}, addEvent: function (element, events, data, callback, live) {
    var $element = $(element);
    if (events = this._formatEvents(events), "object" != typeof data && (callback = data, data = {}), callback = "function" == typeof callback ? $.proxy(callback, this) : $.proxy(this, callback), live ? Test.$document.on(events, element, data, callback) : $element.on(events, data, callback), this._$events = this._$events.add(element), -1 != events.search(/\bactivate\b/) && !$element.data("activatable")) {
        var activateNamespace = this._namespace + "-activate";
        $element.on("mousedown" + activateNamespace, function (event) {
            event.preventDefault()
        }), $element.on("click" + activateNamespace, function (event) {
            event.preventDefault();
            var elemIndex = $.inArray(event.currentTarget, $element), $evElem = $(element[elemIndex]);
            $evElem.hasClass("disabled") || $evElem.trigger("activate")
        }), $element.hasClass("disabled") ? $element.removeAttr("tabindex") : $element.attr("tabindex", "0"), $element.data("activatable", !0)
    }
    if (-1 != events.search(/\btextchange\b/))for (var i = 0; i < $element.length; i++) {
        var _$elem = $($element[i]);
        if (_$elem.data("textchangeValue", _$elem.val()), !_$elem.data("textchangeable")) {
            var textchangeNamespace = this._namespace + "-textchange", events = "keypress" + textchangeNamespace + " keyup" + textchangeNamespace + " change" + textchangeNamespace + " blur" + textchangeNamespace;
            _$elem.on(events, function (ev) {
                var _$elem = $(ev.currentTarget), val = _$elem.val();
                val != _$elem.data("textchangeValue") && (_$elem.data("textchangeValue", val), _$elem.trigger("textchange"))
            }), _$elem.data("textchangeable", !0)
        }
    }
}, removeEvent: function (element, events) {
    events = this._formatEvents(events), $(element).off(events)
}, removeAllEvents: function (element) {
    $(element).off(this._namespace)
}, destroy: function () {
    this.removeAllEvents(this._$events)
}, runMethodOverwrite: function () {
    var args = Array.prototype.slice.call(arguments), functionName = args[0];
    return args.splice(0, 1), void 0 !== this.settings[functionName] && "function" == typeof this.settings[functionName] ? this.settings[functionName].apply(this, args) : typeof this[functionName] == typeof Function ? this[functionName].apply(this, args) : void 0
}, enqueueAjax: function (options) {
    function doAjaxRequest(next) {
        jqXHR = $.ajax(options), jqXHR.done(dfd.resolve).fail(dfd.reject).then(next, next), $(this).dequeue()
    }

    var jqXHR, dfd = $.Deferred(), promise = dfd.promise();
    return this.ajaxQueue.queue(doAjaxRequest), promise.abort = function (statusText) {
        if (jqXHR)return jqXHR.abort(statusText);
        var queue = this.ajaxQueue.queue(), index = $.inArray(doAjaxRequest, queue);
        return index > -1 && queue.splice(index, 1), dfd.rejectWith(options.context || options, [promise, statusText, ""]), promise
    }, promise
}});