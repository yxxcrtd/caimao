define([
], function() {
	var VKI_attach, VKI_close;
	(function() {
	  var self = {};

	  self.VKI_version = "1.49";
	  self.VKI_showVersion = false;
	  self.VKI_target = false;
	  self.VKI_shift = self.VKI_shiftlock = false;
	  self.VKI_altgr = self.VKI_altgrlock = false;
	  self.VKI_dead = false;
	  self.VKI_deadBox = false; // Show the dead keys checkbox
	  self.VKI_deadkeysOn = false;  // Turn dead keys on by default
	  self.VKI_numberPad = true;  // Allow user to open and close the number pad
	  self.VKI_numberPadOn = false;  // Show number pad by default
	  self.VKI_kts = self.VKI_kt = "US International";  // Default keyboard layout
	  self.VKI_langAdapt = true;  // Use lang attribute of input to select keyboard
	  self.VKI_size = 2;  // Default keyboard size (1-5)
	  self.VKI_sizeAdj = true;  // Allow user to adjust keyboard size
	  self.VKI_clearPasswords = false;  // Clear password fields on focus
	  self.VKI_imageURI = '';  // If empty string, use imageless mode
	  self.VKI_clickless = 0;  // 0 = disabled, > 0 = delay in ms
	  self.VKI_activeTab = 0;  // Tab moves to next: 1 = element, 2 = keyboard enabled element
	  self.VKI_enterSubmit = false;  // Submit forms when Enter is pressed
	  self.VKI_keyCenter = 3;

	  self.VKI_isIE = /*@cc_on!@*/false;
	  self.VKI_isIE6 = /*@if(@_jscript_version == 5.6)!@end@*/false;
	  self.VKI_isIElt8 = /*@if(@_jscript_version < 5.8)!@end@*/false;
	  self.VKI_isWebKit = RegExp("KHTML").test(navigator.userAgent);
	  self.VKI_isOpera = RegExp("Opera").test(navigator.userAgent);
	  self.VKI_isMoz = (!self.VKI_isWebKit && navigator.product == "Gecko");

	  /* ***** i18n text strings ************************************* */
	  self.VKI_i18n = {
	    '00': "Display Number Pad",
	    '01': "Display virtual keyboard interface",
	    '02': "Select keyboard layout",
	    '03': "Dead keys",
	    '04': "On",
	    '05': "Off",
	    '06': "Close the keyboard",
	    '07': "Clear",
	    '08': "Clear self input",
	    '09': "Version",
	    '10': "Decrease keyboard size",
	    '11': "Increase keyboard size"
	  };


	  /* ***** Create keyboards ************************************** */
	  self.VKI_layout = {};

	  // - Lay out each keyboard in rows of sub-arrays.  Each sub-array
	  //   represents one key.
	  //
	  // - Each sub-array consists of four slots described as follows:
	  //     example: ["a", "A", "\u00e1", "\u00c1"]
	  //
	  //          a) Normal character
	  //          A) Character + Shift/Caps
	  //     \u00e1) Character + Alt/AltGr/AltLk
	  //     \u00c1) Character + Shift/Caps + Alt/AltGr/AltLk
	  //
	  //   You may include sub-arrays which are fewer than four slots.
	  //   In these cases, the missing slots will be blanked when the
	  //   corresponding modifier key (Shift or AltGr) is pressed.
	  //
	  // - If the second slot of a sub-array matches one of the following
	  //   strings:
	  //     "Tab", "Caps", "Shift", "Enter", "Bksp",
	  //     "Alt" OR "AltGr", "AltLk"
	  //   then the function of the key will be the following,
	  //   respectively:
	  //     - Insert a tab
	  //     - Toggle Caps Lock (technically a Shift Lock)
	  //     - Next entered character will be the shifted character
	  //     - Insert a newline (textarea), or close the keyboard
	  //     - Delete the previous character
	  //     - Next entered character will be the alternate character
	  //     - Toggle Alt/AltGr Lock
	  //
	  //   The first slot of self sub-array will be the text to display
	  //   on the corresponding key.  self allows for easy localisation
	  //   of key names.
	  //
	  // - Layout dead keys (diacritic + letter) should be added as
	  //   property/value pairs of objects with hash keys equal to the
	  //   diacritic.  See the "self.VKI_deadkey" object below the layout
	  //   definitions.  In each property/value pair, the value is what
	  //   the diacritic would change the property name to.
	  //
	  // - Note that any characters beyond the normal ASCII set should be
	  //   entered in escaped Unicode format.  (eg \u00a3 = Pound symbol)
	  //   You can find Unicode values for characters here:
	  //     http://unicode.org/charts/
	  //
	  // - To remove a keyboard, just delete it, or comment it out of the
	  //   source code. If you decide to remove the US International
	  //   keyboard layout, make sure you change the default layout
	  //   (self.VKI_kt) above so it references an existing layout.

	   self.VKI_layout['US International'] = {
	    'name': "US International", 'keys': [
	      [["`", "~"], ["1", "!", "\u00a1", "\u00b9"], ["2", "@", "\u00b2"], ["3", "#", "\u00b3"], ["4", "$", "\u00a4", "\u00a3"], ["5", "%", "\u20ac"], ["6", "^", "\u00bc"], ["7", "&", "\u00bd"], ["8", "*", "\u00be"], ["9", "(", "\u2018"], ["0", ")", "\u2019"], ["-", "_", "\u00a5"], ["=", "+", "\u00d7", "\u00f7"], ["Bksp", "Bksp"]],
	      [["Tab", "Tab"], ["q", "Q", "\u00e4", "\u00c4"], ["w", "W", "\u00e5", "\u00c5"], ["e", "E", "\u00e9", "\u00c9"], ["r", "R", "\u00ae"], ["t", "T", "\u00fe", "\u00de"], ["y", "Y", "\u00fc", "\u00dc"], ["u", "U", "\u00fa", "\u00da"], ["i", "I", "\u00ed", "\u00cd"], ["o", "O", "\u00f3", "\u00d3"], ["p", "P", "\u00f6", "\u00d6"], ["[", "{", "\u00ab"], ["]", "}", "\u00bb"], ["\\", "|", "\u00ac", "\u00a6"]],
	      [["Caps", "Caps"], ["a", "A", "\u00e1", "\u00c1"], ["s", "S", "\u00df", "\u00a7"], ["d", "D", "\u00f0", "\u00d0"], ["f", "F"], ["g", "G"], ["h", "H"], ["j", "J"], ["k", "K"], ["l", "L", "\u00f8", "\u00d8"], [";", ":", "\u00b6", "\u00b0"], ["'", '"', "\u00b4", "\u00a8"], ["Enter", "Enter"]],
	      [["Shift", "Shift"], ["z", "Z", "\u00e6", "\u00c6"], ["x", "X"], ["c", "C", "\u00a9", "\u00a2"], ["v", "V"], ["b", "B"], ["n", "N", "\u00f1", "\u00d1"], ["m", "M", "\u00b5"], [",", "<", "\u00e7", "\u00c7"], [".", ">"], ["/", "?", "\u00bf"], ["Shift", "Shift"]],
	      [[" ", " ", " ", " "], ["Alt", "Alt"]]
	    ], 'lang': ["en"] };


	  /* ***** Define Dead Keys ************************************** */
	  self.VKI_deadkey = {};

	  // - Lay out each dead key set as an object of property/value
	  //   pairs.  The rows below are wrapped so uppercase letters are
	  //   below their lowercase equivalents.
	  //
	  // - The property name is the letter pressed after the diacritic.
	  //   The property value is the letter self key-combo will generate.
	  //
	  // - Note that if you have created a new keyboard layout and want
	  //   it included in the distributed script, PLEASE TELL ME if you
	  //   have added additional dead keys to the ones below.

	  self.VKI_deadkey['"'] = self.VKI_deadkey['\u00a8'] = self.VKI_deadkey['\u309B'] = { // Umlaut / Diaeresis / Greek Dialytika / Hiragana/Katakana Voiced Sound Mark
	    'a': "\u00e4", 'e': "\u00eb", 'i': "\u00ef", 'o': "\u00f6", 'u': "\u00fc", 'y': "\u00ff", '\u03b9': "\u03ca", '\u03c5': "\u03cb", '\u016B': "\u01D6", '\u00FA': "\u01D8", '\u01D4': "\u01DA", '\u00F9': "\u01DC",
	    'A': "\u00c4", 'E': "\u00cb", 'I': "\u00cf", 'O': "\u00d6", 'U': "\u00dc", 'Y': "\u0178", '\u0399': "\u03aa", '\u03a5': "\u03ab", '\u016A': "\u01D5", '\u00DA': "\u01D7", '\u01D3': "\u01D9", '\u00D9': "\u01DB",
	    '\u304b': "\u304c", '\u304d': "\u304e", '\u304f': "\u3050", '\u3051': "\u3052", '\u3053': "\u3054", '\u305f': "\u3060", '\u3061': "\u3062", '\u3064': "\u3065", '\u3066': "\u3067", '\u3068': "\u3069",
	    '\u3055': "\u3056", '\u3057': "\u3058", '\u3059': "\u305a", '\u305b': "\u305c", '\u305d': "\u305e", '\u306f': "\u3070", '\u3072': "\u3073", '\u3075': "\u3076", '\u3078': "\u3079", '\u307b': "\u307c",
	    '\u30ab': "\u30ac", '\u30ad': "\u30ae", '\u30af': "\u30b0", '\u30b1': "\u30b2", '\u30b3': "\u30b4", '\u30bf': "\u30c0", '\u30c1': "\u30c2", '\u30c4': "\u30c5", '\u30c6': "\u30c7", '\u30c8': "\u30c9",
	    '\u30b5': "\u30b6", '\u30b7': "\u30b8", '\u30b9': "\u30ba", '\u30bb': "\u30bc", '\u30bd': "\u30be", '\u30cf': "\u30d0", '\u30d2': "\u30d3", '\u30d5': "\u30d6", '\u30d8': "\u30d9", '\u30db': "\u30dc"
	  };
	  self.VKI_deadkey['~'] = { // Tilde / Stroke
	    'a': "\u00e3", 'l': "\u0142", 'n': "\u00f1", 'o': "\u00f5",
	    'A': "\u00c3", 'L': "\u0141", 'N': "\u00d1", 'O': "\u00d5"
	  };
	  self.VKI_deadkey['^'] = { // Circumflex
	    'a': "\u00e2", 'e': "\u00ea", 'i': "\u00ee", 'o': "\u00f4", 'u': "\u00fb", 'w': "\u0175", 'y': "\u0177",
	    'A': "\u00c2", 'E': "\u00ca", 'I': "\u00ce", 'O': "\u00d4", 'U': "\u00db", 'W': "\u0174", 'Y': "\u0176"
	  };
	  self.VKI_deadkey['\u02c7'] = { // Baltic caron
	    'c': "\u010D", 'd': "\u010f", 'e': "\u011b", 's': "\u0161", 'l': "\u013e", 'n': "\u0148", 'r': "\u0159", 't': "\u0165", 'u': "\u01d4", 'z': "\u017E", '\u00fc': "\u01da",
	    'C': "\u010C", 'D': "\u010e", 'E': "\u011a", 'S': "\u0160", 'L': "\u013d", 'N': "\u0147", 'R': "\u0158", 'T': "\u0164", 'U': "\u01d3", 'Z': "\u017D", '\u00dc': "\u01d9"
	  };
	  self.VKI_deadkey['\u02d8'] = { // Romanian and Turkish breve
	    'a': "\u0103", 'g': "\u011f",
	    'A': "\u0102", 'G': "\u011e"
	  };
	  self.VKI_deadkey['-'] = self.VKI_deadkey['\u00af'] = { // Macron
	    'a': "\u0101", 'e': "\u0113", 'i': "\u012b", 'o': "\u014d", 'u': "\u016B", 'y': "\u0233", '\u00fc': "\u01d6",
	    'A': "\u0100", 'E': "\u0112", 'I': "\u012a", 'O': "\u014c", 'U': "\u016A", 'Y': "\u0232", '\u00dc': "\u01d5"
	  };
	  self.VKI_deadkey['`'] = { // Grave
	    'a': "\u00e0", 'e': "\u00e8", 'i': "\u00ec", 'o': "\u00f2", 'u': "\u00f9", '\u00fc': "\u01dc",
	    'A': "\u00c0", 'E': "\u00c8", 'I': "\u00cc", 'O': "\u00d2", 'U': "\u00d9", '\u00dc': "\u01db"
	  };
	  self.VKI_deadkey["'"] = self.VKI_deadkey['\u00b4'] = self.VKI_deadkey['\u0384'] = { // Acute / Greek Tonos
	    'a': "\u00e1", 'e': "\u00e9", 'i': "\u00ed", 'o': "\u00f3", 'u': "\u00fa", 'y': "\u00fd", '\u03b1': "\u03ac", '\u03b5': "\u03ad", '\u03b7': "\u03ae", '\u03b9': "\u03af", '\u03bf': "\u03cc", '\u03c5': "\u03cd", '\u03c9': "\u03ce", '\u00fc': "\u01d8",
	    'A': "\u00c1", 'E': "\u00c9", 'I': "\u00cd", 'O': "\u00d3", 'U': "\u00da", 'Y': "\u00dd", '\u0391': "\u0386", '\u0395': "\u0388", '\u0397': "\u0389", '\u0399': "\u038a", '\u039f': "\u038c", '\u03a5': "\u038e", '\u03a9': "\u038f", '\u00dc': "\u01d7"
	  };
	  self.VKI_deadkey['\u02dd'] = { // Hungarian Double Acute Accent
	    'o': "\u0151", 'u': "\u0171",
	    'O': "\u0150", 'U': "\u0170"
	  };
	  self.VKI_deadkey['\u0385'] = { // Greek Dialytika + Tonos
	    '\u03b9': "\u0390", '\u03c5': "\u03b0"
	  };
	  self.VKI_deadkey['\u00b0'] = self.VKI_deadkey['\u00ba'] = { // Ring
	    'a': "\u00e5", 'u': "\u016f",
	    'A': "\u00c5", 'U': "\u016e"
	  };
	  self.VKI_deadkey['\u02DB'] = { // Ogonek
	    'a': "\u0106", 'e': "\u0119", 'i': "\u012f", 'o': "\u01eb", 'u': "\u0173", 'y': "\u0177",
	    'A': "\u0105", 'E': "\u0118", 'I': "\u012e", 'O': "\u01ea", 'U': "\u0172", 'Y': "\u0176"
	  };
	  self.VKI_deadkey['\u02D9'] = { // Dot-above
	    'c': "\u010B", 'e': "\u0117", 'g': "\u0121", 'z': "\u017C",
	    'C': "\u010A", 'E': "\u0116", 'G': "\u0120", 'Z': "\u017B"
	  };
	  self.VKI_deadkey['\u00B8'] = self.VKI_deadkey['\u201a'] = { // Cedilla
	    'c': "\u00e7", 's': "\u015F",
	    'C': "\u00c7", 'S': "\u015E"
	  };
	  self.VKI_deadkey[','] = { // Comma
	    's': (self.VKI_isIElt8) ? "\u015F" : "\u0219", 't': (self.VKI_isIElt8) ? "\u0163" : "\u021B",
	    'S': (self.VKI_isIElt8) ? "\u015E" : "\u0218", 'T': (self.VKI_isIElt8) ? "\u0162" : "\u021A"
	  };
	  self.VKI_deadkey['\u3002'] = { // Hiragana/Katakana Point
	    '\u306f': "\u3071", '\u3072': "\u3074", '\u3075': "\u3077", '\u3078': "\u307a", '\u307b': "\u307d",
	    '\u30cf': "\u30d1", '\u30d2': "\u30d4", '\u30d5': "\u30d7", '\u30d8': "\u30da", '\u30db': "\u30dd"
	  };


	  /* ***** Define Symbols **************************************** */
	  self.VKI_symbol = {
	    '\u00a0': "NB\nSP", '\u200b': "ZW\nSP", '\u200c': "ZW\nNJ", '\u200d': "ZW\nJ"
	  };


	  /* ***** Layout Number Pad ************************************* */
	  self.VKI_numpad = [
	    [["$"], ["\u00a3"], ["\u20ac"], ["\u00a5"]],
	    [["7"], ["8"], ["9"], ["/"]],
	    [["4"], ["5"], ["6"], ["*"]],
	    [["1"], ["2"], ["3"], ["-"]],
	    [["0"], ["."], ["="], ["+"]]
	  ];


	  /* ****************************************************************
	   * Attach the keyboard to an element
	   *
	   */
	  VKI_attach = function(elem, image) {
	    if (elem.getAttribute("VKI_attached")) return false;
	    if (image) {
	          image.elem = elem;
	          image.onclick = function(e) {
	            e = e || event;
	            if (e.stopPropagation) { e.stopPropagation(); } else e.cancelBubble = true;
	            self.VKI_show(this.elem);
	          };
	      //elem.parentNode.insertBefore(image, (elem.dir == "rtl") ? elem : elem.nextSibling);
	    } else {
	      elem.onfocus = function() {
	        if (self.VKI_target != self) {
	          if (self.VKI_target) self.VKI_close();
	          self.VKI_show(self);
	        }
	      };
	      elem.onclick = function() {
	        if (!self.VKI_target) self.VKI_show(self);
	      }
	    }
	    elem.setAttribute("VKI_attached", 'true');
	    if (self.VKI_isIE) {
	      elem.onclick = elem.onselect = elem.onkeyup = function(e) {
	        if ((e || event).type != "keyup" || !self.readOnly)
	          self.range = document.selection.createRange();
	      };
	    }
	    VKI_addListener(elem, 'click', function(e) {
	      if (self.VKI_target == self) {
	        e = e || event;
	        if (e.stopPropagation) { e.stopPropagation(); } else e.cancelBubble = true;
	      } return false;
	    }, false);
	    if (self.VKI_isMoz)
	      elem.addEventListener('blur', function() { self.setAttribute('_scrollTop', self.scrollTop); }, false);
	  };


	  /* ***** Find tagged input & textarea elements ***************** */
	  function VKI_buildKeyboardInputs() {
	    var inputElems = [
	      document.getElementsByTagName('input'),
	      document.getElementsByTagName('textarea')
	    ];
	    for (var x = 0, elem; elem = inputElems[x++];)
	      for (var y = 0, ex; ex = elem[y++];)
	        if (ex.nodeName == "TEXTAREA" || ex.type == "text" || ex.type == "password")
					  if (ex.className.indexOf("keyboardInput") > -1) VKI_attach(ex);

	    VKI_addListener(document.documentElement, 'click', function(e) { self.VKI_close(); }, false);
	  }


	  /* ****************************************************************
	   * Common mouse event actions
	   *
	   */
	  function VKI_mouseEvents(elem) {
	    if (elem.nodeName == "TD") {
	      if (!elem.click) elem.click = function() {
	        var evt = self.ownerDocument.createEvent('MouseEvents');
	        evt.initMouseEvent('click', true, true, self.ownerDocument.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
	        self.dispatchEvent(evt);
	      };
	      elem.VKI_clickless = 0;
	      VKI_addListener(elem, 'dblclick', function() { return false; }, false);
	    }
	    VKI_addListener(elem, 'mouseover', function() {
	      if (self.nodeName == "TD" && self.VKI_clickless) {
	        var _self = self;
	        clearTimeout(self.VKI_clickless);
	        self.VKI_clickless = setTimeout(function() { _self.click(); }, self.VKI_clickless);
	      }
	      if (self.VKI_isIE) self.className += " hover";
	    }, false);
	    VKI_addListener(elem, 'mouseout', function() {
	      if (self.nodeName == "TD") clearTimeout(self.VKI_clickless);
	      if (self.VKI_isIE) self.className = self.className.replace(/ ?(hover|pressed) ?/g, "");
	    }, false);
	    VKI_addListener(elem, 'mousedown', function() {
	      if (self.nodeName == "TD") clearTimeout(self.VKI_clickless);
	      if (self.VKI_isIE) self.className += " pressed";
	    }, false);
	    VKI_addListener(elem, 'mouseup', function() {
	      if (self.nodeName == "TD") clearTimeout(self.VKI_clickless);
	      if (self.VKI_isIE) self.className = self.className.replace(/ ?pressed ?/g, "");
	    }, false);
	  }


	  /* ***** Build the keyboard interface ************************** */
	  self.VKI_keyboard = document.createElement('table');
	  self.VKI_keyboard.id = "keyboardInputMaster";
	  self.VKI_keyboard.dir = "ltr";
	  self.VKI_keyboard.cellSpacing = "0";
	  self.VKI_keyboard.reflow = function() {
	    self.style.width = "50px";
	    var foo = self.offsetWidth;
	    self.style.width = "";
	  };
	  VKI_addListener(self.VKI_keyboard, 'click', function(e) {
	    e = e || event;
	    if (e.stopPropagation) { e.stopPropagation(); } else e.cancelBubble = true;
	    return false;
	  }, false);

	  if (!self.VKI_layout[self.VKI_kt])
	    return alert('No keyboard named "' + self.VKI_kt + '"');

	  self.VKI_langCode = {};
	  var thead = document.createElement('thead');
	    var tr = document.createElement('tr');
	      var th = document.createElement('th');
	          th.colSpan = "2";

	        var kbSelect = document.createElement('div');
	            kbSelect.title = self.VKI_i18n['02'];
	          VKI_addListener(kbSelect, 'click', function() {
	            var ol = this.getElementsByTagName('ol')[0];
	            if (!ol.style.display) {
	                ol.style.display = "block";
	              var li = ol.getElementsByTagName('li');
	              for (var x = 0, scr = 0; x < li.length; x++) {
	                if (VKI_kt == li[x].firstChild.nodeValue) {
	                  li[x].className = "selected";
	                  scr = li[x].offsetTop - li[x].offsetHeight * 2;
	                } else li[x].className = "";
	              } setTimeout(function() { ol.scrollTop = scr; }, 0);
	            } else ol.style.display = "";
	          }, false);
	            kbSelect.appendChild(document.createTextNode(self.VKI_kt));
	            kbSelect.appendChild(document.createTextNode(self.VKI_isIElt8 ? " \u2193" : " \u25be"));
	            kbSelect.langCount = 0;
	          var ol = document.createElement('ol');
	            for (ktype in self.VKI_layout) {
	              if (typeof self.VKI_layout[ktype] == "object") {
	                if (!self.VKI_layout[ktype].lang) self.VKI_layout[ktype].lang = [];
	                for (var x = 0; x < self.VKI_layout[ktype].lang.length; x++)
	                  self.VKI_langCode[self.VKI_layout[ktype].lang[x].toLowerCase().replace(/-/g, "_")] = ktype;
	                var li = document.createElement('li');
	                    li.title = self.VKI_layout[ktype].name;
	                  VKI_addListener(li, 'click', function(e) {
	                    e = e || event;
	                    if (e.stopPropagation) { e.stopPropagation(); } else e.cancelBubble = true;
	                    this.parentNode.style.display = "";
	                    self.VKI_kts = self.VKI_kt = kbSelect.firstChild.nodeValue = this.firstChild.nodeValue;
	                    self.VKI_buildKeys();
	                    self.VKI_position(true);
	                  }, false);
	                  VKI_mouseEvents(li);
	                    li.appendChild(document.createTextNode(ktype));
	                  ol.appendChild(li);
	                kbSelect.langCount++;
	              }
	            } kbSelect.appendChild(ol);
	          if (kbSelect.langCount > 1) th.appendChild(kbSelect);
	        self.VKI_langCode.index = [];
	        for (prop in self.VKI_langCode)
	          if (prop != "index" && typeof self.VKI_langCode[prop] == "string")
	            self.VKI_langCode.index.push(prop);
	        self.VKI_langCode.index.sort();
	        self.VKI_langCode.index.reverse();

	        if (self.VKI_numberPad) {
	          var span = document.createElement('span');
	              span.appendChild(document.createTextNode("#"));
	              span.title = self.VKI_i18n['00'];
	            VKI_addListener(span, 'click', function() {
	              kbNumpad.style.display = (!kbNumpad.style.display) ? "none" : "";
	              self.VKI_position(true);
	            }, false);
	            VKI_mouseEvents(span);
	            th.appendChild(span);
	        }

	        self.VKI_kbsize = function(e) {
	          self.VKI_size = Math.min(5, Math.max(1, self.VKI_size));
	          self.VKI_keyboard.className = self.VKI_keyboard.className.replace(/ ?keyboardInputSize\d ?/, "");
	          if (self.VKI_size != 2) self.VKI_keyboard.className += " keyboardInputSize" + self.VKI_size;
	          self.VKI_position(true);
	          if (self.VKI_isOpera) self.VKI_keyboard.reflow();
	        };
	        if (self.VKI_sizeAdj) {
	          var small = document.createElement('small');
	              small.title = self.VKI_i18n['10'];
	            VKI_addListener(small, 'click', function() {
	              --self.VKI_size;
	              self.VKI_kbsize();
	            }, false);
	            VKI_mouseEvents(small);
	              small.appendChild(document.createTextNode(self.VKI_isIElt8 ? "\u2193" : "\u21d3"));
	            th.appendChild(small);
	          var big = document.createElement('big');
	              big.title = self.VKI_i18n['11'];
	            VKI_addListener(big, 'click', function() {
	              ++self.VKI_size;
	              self.VKI_kbsize();
	            }, false);
	            VKI_mouseEvents(big);
	              big.appendChild(document.createTextNode(self.VKI_isIElt8 ? "\u2191" : "\u21d1"));
	            th.appendChild(big);
	        }

	        var span = document.createElement('span');
	            span.appendChild(document.createTextNode(self.VKI_i18n['07']));
	            span.title = self.VKI_i18n['08'];
	          VKI_addListener(span, 'click', function() {
	            self.VKI_target.value = "";
	            self.VKI_target.focus();
	            return false;
	          }, false);
	          VKI_mouseEvents(span);
	          th.appendChild(span);

	        var strong = document.createElement('strong');
	            strong.appendChild(document.createTextNode('X'));
	            strong.title = self.VKI_i18n['06'];
	          VKI_addListener(strong, 'click', function() { self.VKI_close(); }, false);
	          VKI_mouseEvents(strong);
	          th.appendChild(strong);

	        tr.appendChild(th);
	      thead.appendChild(tr);
	  self.VKI_keyboard.appendChild(thead);

	  var tbody = document.createElement('tbody');
	    var tr = document.createElement('tr');
	      var td = document.createElement('td');
	        var div = document.createElement('div');

	        if (self.VKI_deadBox) {
	          var label = document.createElement('label');
	            var checkbox = document.createElement('input');
	                checkbox.type = "checkbox";
	                checkbox.title = self.VKI_i18n['03'] + ": " + ((self.VKI_deadkeysOn) ? self.VKI_i18n['04'] : self.VKI_i18n['05']);
	                checkbox.defaultChecked = self.VKI_deadkeysOn;
	              VKI_addListener(checkbox, 'click', function() {
	                self.title = self.VKI_i18n['03'] + ": " + ((self.checked) ? self.VKI_i18n['04'] : self.VKI_i18n['05']);
	                self.VKI_modify("");
	                return true;
	              }, false);
	              label.appendChild(checkbox);
	                checkbox.checked = self.VKI_deadkeysOn;
	            div.appendChild(label);
	          self.VKI_deadkeysOn = checkbox;
	        } else self.VKI_deadkeysOn.checked = self.VKI_deadkeysOn;

	        if (self.VKI_showVersion) {
	          var vr = document.createElement('var');
	              vr.title = self.VKI_i18n['09'] + " " + self.VKI_version;
	              vr.appendChild(document.createTextNode("v" + self.VKI_version));
	            div.appendChild(vr);
	        } td.appendChild(div);
	        tr.appendChild(td);

	      var kbNumpad = document.createElement('td');
	          kbNumpad.id = "keyboardInputNumpad";
	        if (!self.VKI_numberPadOn) kbNumpad.style.display = "none";
	        var ntable = document.createElement('table');
	            ntable.cellSpacing = "0";
	          var ntbody = document.createElement('tbody');
	            for (var x = 0; x < self.VKI_numpad.length; x++) {
	              var ntr = document.createElement('tr');
	                for (var y = 0; y < self.VKI_numpad[x].length; y++) {
	                  var ntd = document.createElement('td');
	                    VKI_addListener(ntd, 'click', VKI_keyClick, false);
	                    VKI_mouseEvents(ntd);
	                      ntd.appendChild(document.createTextNode(self.VKI_numpad[x][y]));
	                    ntr.appendChild(ntd);
	                } ntbody.appendChild(ntr);
	            } ntable.appendChild(ntbody);
	          kbNumpad.appendChild(ntable);
	        tr.appendChild(kbNumpad);
	      tbody.appendChild(tr);
	  self.VKI_keyboard.appendChild(tbody);

	  if (self.VKI_isIE6) {
	    self.VKI_iframe = document.createElement('iframe');
	    self.VKI_iframe.style.position = "absolute";
	    self.VKI_iframe.style.border = "0px none";
	    self.VKI_iframe.style.filter = "mask()";
	    self.VKI_iframe.style.zIndex = "999999";
	    self.VKI_iframe.src = self.VKI_imageURI;
	  }


	  /* ****************************************************************
	   * Private table cell attachment function for generic characters
	   *
	   */
	  function VKI_keyClick() {
	    var done = false, character = "\xa0";
	    if (this.firstChild.nodeName.toLowerCase() != "small") {
	      if ((character = this.firstChild.nodeValue) == "\xa0") return false;
	    } else character = this.firstChild.getAttribute('char');
	    if (self.VKI_deadkeysOn.checked && self.VKI_dead) {
	      if (self.VKI_dead != character) {
	        if (character != " ") {
	          if (self.VKI_deadkey[self.VKI_dead][character]) {
	            self.VKI_insert(self.VKI_deadkey[self.VKI_dead][character]);
	            done = true;
	          }
	        } else {
	          self.VKI_insert(self.VKI_dead);
	          done = true;
	        }
	      } else done = true;
	    } self.VKI_dead = false;

	    if (!done) {
	      if (self.VKI_deadkeysOn.checked && self.VKI_deadkey[character]) {
	        self.VKI_dead = character;
	        self.className += " dead";
	        if (self.VKI_shift) self.VKI_modify("Shift");
	        if (self.VKI_altgr) self.VKI_modify("AltGr");
	      } else self.VKI_insert(character);
	    } self.VKI_modify("");
	    return false;
	  }


	  /* ****************************************************************
	   * Build or rebuild the keyboard keys
	   *
	   */
	  self.VKI_buildKeys = function() {
	    self.VKI_shift = self.VKI_shiftlock = self.VKI_altgr = self.VKI_altgrlock = self.VKI_dead = false;
	    var container = self.VKI_keyboard.tBodies[0].getElementsByTagName('div')[0];
	    var tables = container.getElementsByTagName('table');
	    for (var x = tables.length - 1; x >= 0; x--) container.removeChild(tables[x]);

	    for (var x = 0, hasDeadKey = false, lyt; lyt = self.VKI_layout[self.VKI_kt].keys[x++];) {
	      var table = document.createElement('table');
	          table.cellSpacing = "0";
	        if (lyt.length <= self.VKI_keyCenter) table.className = "keyboardInputCenter";
	        var tbody = document.createElement('tbody');
	          var tr = document.createElement('tr');
	            for (var y = 0, lkey; lkey = lyt[y++];) {
	              var td = document.createElement('td');
	                if (self.VKI_symbol[lkey[0]]) {
	                  var text = self.VKI_symbol[lkey[0]].split("\n");
	                  var small = document.createElement('small');
	                      small.setAttribute('char', lkey[0]);
	                  for (var z = 0; z < text.length; z++) {
	                    if (z) small.appendChild(document.createElement("br"));
	                    small.appendChild(document.createTextNode(text[z]));
	                  } td.appendChild(small);
	                } else td.appendChild(document.createTextNode(lkey[0] || "\xa0"));

	                var className = [];
	                if (self.VKI_deadkeysOn.checked)
	                  for (key in self.VKI_deadkey)
	                    if (key === lkey[0]) { className.push("deadkey"); break; }
	                if (lyt.length > self.VKI_keyCenter && y == lyt.length) className.push("last");
	                if (lkey[0] == " " || lkey[1] == " ") className.push("space");
	                  td.className = className.join(" ");

	                switch (lkey[1]) {
	                  case "Caps": case "Shift":
	                  case "Alt": case "AltGr": case "AltLk":
	                    VKI_addListener(td, 'click', (function(type) { return function() { self.VKI_modify(type); return false; }})(lkey[1]), false);
	                    break;
	                  case "Tab":
	                    VKI_addListener(td, 'click', function() {
	                      if (self.VKI_activeTab) {
	                        if (self.VKI_target.form) {
	                          var target = self.VKI_target, elems = target.form.elements;
	                          self.VKI_close();
	                          for (var z = 0, me = false, j = -1; z < elems.length; z++) {
	                            if (j == -1 && elems[z].getAttribute("VKI_attached")) j = z;
	                            if (me) {
	                              if (self.VKI_activeTab == 1 && elems[z]) break;
	                              if (elems[z].getAttribute("VKI_attached")) break;
	                            } else if (elems[z] == target) me = true;
	                          } if (z == elems.length) z = Math.max(j, 0);
	                          if (elems[z].getAttribute("VKI_attached")) {
	                            self.VKI_show(elems[z]);
	                          } else elems[z].focus();
	                        } else self.VKI_target.focus();
	                      } else self.VKI_insert("\t");
	                      return false;
	                    }, false);
	                    break;
	                  case "Bksp":
	                    VKI_addListener(td, 'click', function() {
	                      self.VKI_target.focus();
	                      if (self.VKI_target.setSelectionRange && !self.VKI_target.readOnly) {
	                        var rng = [self.VKI_target.selectionStart, self.VKI_target.selectionEnd];
	                        if (rng[0] < rng[1]) rng[0]++;
	                        self.VKI_target.value = self.VKI_target.value.substr(0, rng[0] - 1) + self.VKI_target.value.substr(rng[1]);
	                        self.VKI_target.setSelectionRange(rng[0] - 1, rng[0] - 1);
	                      } else if (self.VKI_target.createTextRange && !self.VKI_target.readOnly) {
	                        try {
	                          self.VKI_target.range.select();
	                        } catch(e) { self.VKI_target.range = document.selection.createRange(); }
	                        if (!self.VKI_target.range.text.length) self.VKI_target.range.moveStart('character', -1);
	                        self.VKI_target.range.text = "";
	                      } else self.VKI_target.value = self.VKI_target.value.substr(0, self.VKI_target.value.length - 1);
	                      if (self.VKI_shift) self.VKI_modify("Shift");
	                      if (self.VKI_altgr) self.VKI_modify("AltGr");
	                      self.VKI_target.focus();
	                      return true;
	                    }, false);
	                    break;
	                  case "Enter":
	                    VKI_addListener(td, 'click', function() {
	                      if (self.VKI_target.nodeName != "TEXTAREA") {
	                        if (self.VKI_enterSubmit && self.VKI_target.form) {
	                          for (var z = 0, subm = false; z < self.VKI_target.form.elements.length; z++)
	                            if (self.VKI_target.form.elements[z].type == "submit") subm = true;
	                          if (!subm) self.VKI_target.form.submit();
	                        }
	                        self.VKI_close();
	                      } else self.VKI_insert("\n");
	                      return true;
	                    }, false);
	                    break;
	                  default:
	                    VKI_addListener(td, 'click', VKI_keyClick, false);

	                } VKI_mouseEvents(td);
	                tr.appendChild(td);
	              for (var z = 0; z < 4; z++)
	                if (self.VKI_deadkey[lkey[z] = lkey[z] || ""]) hasDeadKey = true;
	            } tbody.appendChild(tr);
	          table.appendChild(tbody);
	        container.appendChild(table);
	    }
	    if (self.VKI_deadBox)
	      self.VKI_deadkeysOn.style.display = (hasDeadKey) ? "inline" : "none";
	    if (self.VKI_isIE6) {
	      self.VKI_iframe.style.width = self.VKI_keyboard.offsetWidth + "px";
	      self.VKI_iframe.style.height = self.VKI_keyboard.offsetHeight + "px";
	    }
	  };

	  self.VKI_buildKeys();
	  VKI_addListener(self.VKI_keyboard, 'selectstart', function() { return false; }, false);
	  self.VKI_keyboard.unselectable = "on";
	  if (self.VKI_isOpera)
	    VKI_addListener(self.VKI_keyboard, 'mousedown', function() { return false; }, false);


	  /* ****************************************************************
	   * Controls modifier keys
	   *
	   */
	  self.VKI_modify = function(type) {
	    switch (type) {
	      case "Alt":
	      case "AltGr": self.VKI_altgr = !self.VKI_altgr; break;
	      case "AltLk": self.VKI_altgr = 0; self.VKI_altgrlock = !self.VKI_altgrlock; break;
	      case "Caps": self.VKI_shift = 0; self.VKI_shiftlock = !self.VKI_shiftlock; break;
	      case "Shift": self.VKI_shift = !self.VKI_shift; break;
	    } var vchar = 0;
	    if (!self.VKI_shift != !self.VKI_shiftlock) vchar += 1;
	    if (!self.VKI_altgr != !self.VKI_altgrlock) vchar += 2;

	    var tables = self.VKI_keyboard.tBodies[0].getElementsByTagName('div')[0].getElementsByTagName('table');
	    for (var x = 0; x < tables.length; x++) {
	      var tds = tables[x].getElementsByTagName('td');
	      for (var y = 0; y < tds.length; y++) {
	        var className = [], lkey = self.VKI_layout[self.VKI_kt].keys[x][y];

	        switch (lkey[1]) {
	          case "Alt":
	          case "AltGr":
	            if (self.VKI_altgr) className.push("pressed");
	            break;
	          case "AltLk":
	            if (self.VKI_altgrlock) className.push("pressed");
	            break;
	          case "Shift":
	            if (self.VKI_shift) className.push("pressed");
	            break;
	          case "Caps":
	            if (self.VKI_shiftlock) className.push("pressed");
	            break;
	          case "Tab": case "Enter": case "Bksp": break;
	          default:
	            if (type) {
	              tds[y].removeChild(tds[y].firstChild);
	              if (self.VKI_symbol[lkey[vchar]]) {
	                var text = self.VKI_symbol[lkey[vchar]].split("\n");
	                var small = document.createElement('small');
	                    small.setAttribute('char', lkey[vchar]);
	                for (var z = 0; z < text.length; z++) {
	                  if (z) small.appendChild(document.createElement("br"));
	                  small.appendChild(document.createTextNode(text[z]));
	                } tds[y].appendChild(small);
	              } else tds[y].appendChild(document.createTextNode(lkey[vchar] || "\xa0"));
	            }
	            if (self.VKI_deadkeysOn.checked) {
	              var character = tds[y].firstChild.nodeValue || tds[y].firstChild.className;
	              if (self.VKI_dead) {
	                if (character == self.VKI_dead) className.push("pressed");
	                if (self.VKI_deadkey[self.VKI_dead][character]) className.push("target");
	              }
	              if (self.VKI_deadkey[character]) className.push("deadkey");
	            }
	        }

	        if (y == tds.length - 1 && tds.length > self.VKI_keyCenter) className.push("last");
	        if (lkey[0] == " " || lkey[1] == " ") className.push("space");
	        tds[y].className = className.join(" ");
	      }
	    }
	  };


	  /* ****************************************************************
	   * Insert text at the cursor
	   *
	   */
	  self.VKI_insert = function(text) {
	    self.VKI_target.focus();
	    if (self.VKI_target.maxLength) self.VKI_target.maxlength = self.VKI_target.maxLength;
	    if (typeof self.VKI_target.maxlength == "undefined" ||
	        self.VKI_target.maxlength < 0 ||
	        self.VKI_target.value.length < self.VKI_target.maxlength) {
	      if (self.VKI_target.setSelectionRange && !self.VKI_target.readOnly && !self.VKI_isIE) {
	        var rng = [self.VKI_target.selectionStart, self.VKI_target.selectionEnd];
	        self.VKI_target.value = self.VKI_target.value.substr(0, rng[0]) + text + self.VKI_target.value.substr(rng[1]);
	        if (text == "\n" && self.VKI_isOpera) rng[0]++;
	        self.VKI_target.setSelectionRange(rng[0] + text.length, rng[0] + text.length);
	      } else if (self.VKI_target.createTextRange && !self.VKI_target.readOnly) {
	        try {
	          self.VKI_target.range.select();
	        } catch(e) { self.VKI_target.range = document.selection.createRange(); }
	        self.VKI_target.range.text = text;
	        self.VKI_target.range.collapse(true);
	        self.VKI_target.range.select();
	      } else self.VKI_target.value += text;
	      if (self.VKI_shift) self.VKI_modify("Shift");
	      if (self.VKI_altgr) self.VKI_modify("AltGr");
	      self.VKI_target.focus();
	    } else if (self.VKI_target.createTextRange && self.VKI_target.range)
	      self.VKI_target.range.select();
	  };


	  /* ****************************************************************
	   * Show the keyboard interface
	   *
	   */
	  self.VKI_show = function(elem) {
	    if (!self.VKI_target) {
	      self.VKI_target = elem;
	      if (self.VKI_langAdapt && self.VKI_target.lang) {
	        var chg = false, sub = [], lang = self.VKI_target.lang.toLowerCase().replace(/-/g, "_");
	        for (var x = 0, chg = false; !chg && x < self.VKI_langCode.index.length; x++)
	          if (lang.indexOf(self.VKI_langCode.index[x]) == 0)
	            chg = kbSelect.firstChild.nodeValue = self.VKI_kt = self.VKI_langCode[self.VKI_langCode.index[x]];
	        if (chg) self.VKI_buildKeys();
	      }
	      if (self.VKI_isIE) {
	        if (!self.VKI_target.range) {
	          self.VKI_target.range = self.VKI_target.createTextRange();
	          self.VKI_target.range.moveStart('character', self.VKI_target.value.length);
	        } self.VKI_target.range.select();
	      }
	      try { self.VKI_keyboard.parentNode.removeChild(self.VKI_keyboard); } catch (e) {}
	      if (self.VKI_clearPasswords && self.VKI_target.type == "password") self.VKI_target.value = "";

	      var elem = self.VKI_target;
	      self.VKI_target.keyboardPosition = "absolute";
	      do {
	        if (VKI_getStyle(elem, "position") == "fixed") {
	          self.VKI_target.keyboardPosition = "fixed";
	          break;
	        }
	      } while (elem = elem.offsetParent);

	      if (self.VKI_isIE6) document.body.appendChild(self.VKI_iframe);
	      document.body.appendChild(self.VKI_keyboard);
	      self.VKI_keyboard.style.position = self.VKI_target.keyboardPosition;
	      if (self.VKI_isOpera) self.VKI_keyboard.reflow();

	      self.VKI_position(true);
	      if (self.VKI_isMoz || self.VKI_isWebKit) self.VKI_position(true);
	      self.VKI_target.blur();
	      self.VKI_target.focus();
	    } else self.VKI_close();
	  };


	  /* ****************************************************************
	   * Position the keyboard
	   *
	   */
	  self.VKI_position = function(force) {
	    if (self.VKI_target) {
	      var kPos = VKI_findPos(self.VKI_keyboard), wDim = VKI_innerDimensions(), sDis = VKI_scrollDist();
	      var place = false, fudge = self.VKI_target.offsetHeight + 3;
	      if (force !== true) {
	        if (kPos[1] + self.VKI_keyboard.offsetHeight - sDis[1] - wDim[1] > 0) {
	          place = true;
	          fudge = -self.VKI_keyboard.offsetHeight - 3;
	        } else if (kPos[1] - sDis[1] < 0) place = true;
	      }
	      if (place || force === true) {
	        var iPos = VKI_findPos(self.VKI_target), scr = self.VKI_target;
	        while (scr = scr.parentNode) {
	          if (scr == document.body) break;
	          if (scr.scrollHeight > scr.offsetHeight || scr.scrollWidth > scr.offsetWidth) {
	            if (!scr.getAttribute("VKI_scrollListener")) {
	              scr.setAttribute("VKI_scrollListener", true);
	              VKI_addListener(scr, 'scroll', function() { self.VKI_position(true); }, false);
	            } // Check if the input is in view
	            var pPos = VKI_findPos(scr), oTop = iPos[1] - pPos[1], oLeft = iPos[0] - pPos[0];
	            var top = oTop + self.VKI_target.offsetHeight;
	            var left = oLeft + self.VKI_target.offsetWidth;
	            var bottom = scr.offsetHeight - oTop - self.VKI_target.offsetHeight;
	            var right = scr.offsetWidth - oLeft - self.VKI_target.offsetWidth;
	            self.VKI_keyboard.style.display = (top < 0 || left < 0 || bottom < 0 || right < 0) ? "none" : "";
	            if (self.VKI_isIE6) self.VKI_iframe.style.display = (top < 0 || left < 0 || bottom < 0 || right < 0) ? "none" : "";
	          }
	        }
	        self.VKI_keyboard.style.top = 5 + iPos[1] - ((self.VKI_target.keyboardPosition == "fixed" && !self.VKI_isIE && !self.VKI_isMoz) ? sDis[1] : 0) + fudge + "px";
	        self.VKI_keyboard.style.left = Math.max(10, Math.min(wDim[0] - self.VKI_keyboard.offsetWidth - 25, iPos[0])) - 5 + "px";
	        if (self.VKI_isIE6) {
	          self.VKI_iframe.style.width = self.VKI_keyboard.offsetWidth + "px";
	          self.VKI_iframe.style.height = self.VKI_keyboard.offsetHeight + "px";
	          self.VKI_iframe.style.top = self.VKI_keyboard.style.top;
	          self.VKI_iframe.style.left = self.VKI_keyboard.style.left;
	        }
	      }
	      if (force === true) self.VKI_position();
	    }
	  };


	  /* ****************************************************************
	   * Close the keyboard interface
	   *
	   */
	  self.VKI_close = function() {
	    if (self.VKI_target) {
	      try {
	        self.VKI_keyboard.parentNode.removeChild(self.VKI_keyboard);
	        if (self.VKI_isIE6) self.VKI_iframe.parentNode.removeChild(self.VKI_iframe);
	      } catch (e) {}
	      if (self.VKI_kt != self.VKI_kts) {
	        kbSelect.firstChild.nodeValue = self.VKI_kt = self.VKI_kts;
	        self.VKI_buildKeys();
	      } kbSelect.getElementsByTagName('ol')[0].style.display = "";;
	      self.VKI_target.focus();
	      if (self.VKI_isIE) {
	        setTimeout(function() { self.VKI_target = false; }, 0);
	      } else self.VKI_target = false;
	    }
	  };
	  
	  VKI_close = function() {
		self.VKI_close.call(self);  
	  };

	  /* ***** Private functions *************************************** */
	  function VKI_addListener(elem, type, func, cap) {
	    if (elem.addEventListener) {
	      elem.addEventListener(type, function(e) { func.call(elem, e); }, cap);
	    } else if (elem.attachEvent)
	      elem.attachEvent('on' + type, function() { func.call(elem); });
	  }

	  function VKI_findPos(obj) {
	    var curleft = curtop = 0, scr = obj;
	    while ((scr = scr.parentNode) && scr != document.body) {
	      curleft -= scr.scrollLeft || 0;
	      curtop -= scr.scrollTop || 0;
	    }
	    do {
	      curleft += obj.offsetLeft;
	      curtop += obj.offsetTop;
	    } while (obj = obj.offsetParent);
	    return [curleft, curtop];
	  }

	  function VKI_innerDimensions() {
	    if (self.innerHeight) {
	      return [self.innerWidth, self.innerHeight];
	    } else if (document.documentElement && document.documentElement.clientHeight) {
	      return [document.documentElement.clientWidth, document.documentElement.clientHeight];
	    } else if (document.body)
	      return [document.body.clientWidth, document.body.clientHeight];
	    return [0, 0];
	  }

	  function VKI_scrollDist() {
	    var html = document.getElementsByTagName('html')[0];
	    if (html.scrollTop && document.documentElement.scrollTop) {
	      return [html.scrollLeft, html.scrollTop];
	    } else if (html.scrollTop || document.documentElement.scrollTop) {
	      return [html.scrollLeft + document.documentElement.scrollLeft, html.scrollTop + document.documentElement.scrollTop];
	    } else if (document.body.scrollTop)
	      return [document.body.scrollLeft, document.body.scrollTop];
	    return [0, 0];
	  }

	  function VKI_getStyle(obj, styleProp) {
	    if (obj.currentStyle) {
	      var y = obj.currentStyle[styleProp];
	    } else if (window.getComputedStyle)
	      var y = window.getComputedStyle(obj, null)[styleProp];
	    return y;
	  }


	  VKI_addListener(window, 'resize', self.VKI_position, false);
	  VKI_addListener(window, 'scroll', self.VKI_position, false);
	  self.VKI_kbsize();
	  VKI_addListener(window, 'load', VKI_buildKeyboardInputs, false);
	  // VKI_addListener(window, 'load', function() {
	  //   setTimeout(VKI_buildKeyboardInputs, 5);
	  // }, false);
	})();
	return {
		attach: VKI_attach,
		close: VKI_close
	};
});