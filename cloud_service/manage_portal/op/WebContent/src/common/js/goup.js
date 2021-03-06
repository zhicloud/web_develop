/**
 * Created by songgk on 2015/5/15.
 */
(function(e) {
	function t(e, t, n) {
		if (t == "show") {
			switch (n) {
			case "fade":
				e.fadeIn();
				break;
			case "slide":
				e.slideDown();
				break;
			default:
				e.fadeIn()
			}
		} else {
			switch (n) {
			case "fade":
				e.fadeOut();
				break;
			case "slide":
				e.slideUp();
				break;
			default:
				e.fadeOut()
			}
		}
	}
	e.goup = function(n) {
		var r = e.extend({
			location: "right",
			locationOffset: 20,
			bottomOffset: 10,
			containerRadius: 10,
			containerClass: "goup-container",
			arrowClass: "goup-arrow",
			alwaysVisible: false,
			trigger: 500,
			entryAnimation: "fade",
			goupSpeed: "slow",
			hideUnderWidth: 500,
			containerColor: "#219eff",
			arrowColor: "#fff",
			title: "",
			titleAsText: false,
			titleAsTextClass: "goup-text"
		}, n);
		e("body").append('<div style="display:none;z-index:9999;" class="' + r.containerClass + '"></div>');
		var i = e("." + r.containerClass);
		e(i).html('<div class="' + r.arrowClass + '"></div>');
		var s = e("." + r.arrowClass);
		var o = r.location;
		if (o != "right" && o != "left") {
			o = "right"
		}
		var u = r.locationOffset;
		if (u < 0) {
			u = 0
		}
		var a = r.bottomOffset;
		if (a < 0) {
			a = 0
		}
		var f = r.containerRadius;
		if (f < 0) {
			f = 0
		}
		var l = r.trigger;
		if (l < 0) {
			l = 0
		}
		var c = r.hideUnderWidth;
		if (c < 0) {
			c = 0
		}
		var h = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i;
		if (h.test(r.containerColor)) {
			var p = r.containerColor
		} else {
			var p = "#000"
		}
		if (h.test(r.arrowColor)) {
			var d = r.arrowColor
		} else {
			var d = "#fff"
		}
		if (r.title === "") {
			r.titleAsText = false
		}
		var v = {};
		v = {
			position: "fixed",
			width: 40,
			height: 40,
			background: p,
			cursor: "pointer"
		};
		v["bottom"] = a;
		v[o] = u;
		v["border-radius"] = f;
		e(i).css(v);
		if (!r.titleAsText) {
			e(i).attr("title", r.title)
		} else {
			e("body").append('<div class="' + r.titleAsTextClass + '">' + r.title + "</div>");
			var m = e("." + r.titleAsTextClass);
			e(m).attr("style", e(i).attr("style"));
			e(m).css("font-size", 12).css("background", "transparent").css("width", 80).css("height", "auto").css("text-align", "center").css(o, u - 20);
			var g = e(m).height() + 10;
			e(i).css("bottom", "+=" + g + "px")
		}
		var y = {};
		y = {
			width: 0,
			height: 0,
			margin: "0 auto",
			"padding-top": 13,
			"border-style": "solid",
			"border-width": "0 10px 10px 10px",
			"border-color": "transparent transparent " + d + " transparent"
		};
		e(s).css(y);
		var b = false;
		e(window).resize(function() {
			if (e(window).outerWidth() <= c) {
				b = true;
				t(e(i), "hide", r.entryAnimation);
				if (m) t(e(m), "hide", r.entryAnimation)
			} else {
				b = false;
				e(window).trigger("scroll")
			}
		});
		if (e(window).outerWidth() <= c) {
			b = true;
			e(i).hide();
			if (m) e(m).hide()
		}
		if (!r.alwaysVisible) {
			e(window).scroll(function() {
				if (e(window).scrollTop() >= l && !b) {
					t(e(i), "show", r.entryAnimation);
					if (m) t(e(m), "show", r.entryAnimation)
				}
				if (e(window).scrollTop() < l && !b) {
					t(e(i), "hide", r.entryAnimation);
					if (m) t(e(m), "hide", r.entryAnimation)
				}
			})
		} else {
			t(e(i), "show", r.entryAnimation);
			if (m) t(e(m), "show", r.entryAnimation)
		}
		if (e(window).scrollTop() >= l && !b) {
			t(e(i), "show", r.entryAnimation);
			if (m) t(e(m), "show", r.entryAnimation)
		}
		e(i).on("click", function() {
			e("html,body").animate({
				scrollTop: 0
			}, r.goupSpeed);
			return false
		});
		e(m).on("click", function() {
			e("html,body").animate({
				scrollTop: 0
			}, r.goupSpeed);
			return false
		})
	}
})(jQuery)