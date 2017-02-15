$(function() {
  /* Push the body and the nav over by 285px over */
  $('.icon-menu').click(function() {
    $('.menu').animate({
      left: "0px"
    }, 200);

    $('body').animate({
      left: "90px"
    }, 200);
  });
  
    
  /* Then push them back */
  $('.icon-close').click(function() {
    $('.menu').animate({
      left: "-90px"
    }, 200);

    $('body').animate({
      left: "0px"
    }, 200);
  });

  $('a [href*="#"]:not([href="#"])').click(function() {
    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
      if (target.length) {
        $('html, body').animate({
          scrollTop: target.offset().top
        }, 1000);
        return false;
      }
    }
  });

});

$(function writeMessage(msg) {
    var msgDiv = document.getElementById("result");
    msgDiv.innerHtml = msg;
});

$(function(){
    var old = console.log;
    var logger = document.getElementById('result');
    console.log = function (message) {
    if (typeof message == 'object') {
        logger.innerHTML += (JSON && JSON.stringify ? JSON.stringify(message) : message) + '<br />';
        } else {
        logger.innerHTML += message + '<br />';
        }
    }
});


/* Stock Information */
$(function($) {
			$.fn.jStockTicker = function(options) {
				if (typeof (options) == 'undefined') {
					options = {};
				}
				var settings = $.extend( {}, $.fn.jStockTicker.defaults, options);
				var $ticker = $(this);
				var $wrap = null;
				settings.tickerID = $ticker[0].id;
				$.fn.jStockTicker.settings[settings.tickerID] = {};

				if ($ticker.parent().get(0).className != 'wrap') {
					$wrap = $ticker.wrap("<div class='wrap'></div>");
				}

				var $tickerContainer = null;
				if ($ticker.parent().parent().get(0).className != 'container') {
					$tickerContainer = $ticker.parent().wrap(
							"<div class='container'></div>");
				}
				
				var node = $ticker[0].firstChild;
				var next;
				while(node) {
					next = node.nextSibling;
					if(node.nodeType == 3) {
						$ticker[0].removeChild(node);
					}
					node = next;
				}
				
				var shiftLeftAt = $ticker.children().get(0).offsetWidth;
				$.fn.jStockTicker.settings[settings.tickerID].shiftLeftAt = shiftLeftAt;
				$.fn.jStockTicker.settings[settings.tickerID].left = 0;
				$.fn.jStockTicker.settings[settings.tickerID].runid = null;
				$ticker.width(2 * screen.availWidth);
				
				function startTicker() {
					stopTicker();
					
					var params = $.fn.jStockTicker.settings[settings.tickerID]; 
					params.left -= settings.speed;
					if(params.left <= params.shiftLeftAt * -1) {
						params.left = 0;
						$ticker.append($ticker.children().get(0));
						params.shiftLeftAt = $ticker.children().get(0).offsetWidth;
					}
					
					$ticker.css('left', params.left + 'px');
					params.runId = setTimeout(arguments.callee, settings.interval);
					
					$.fn.jStockTicker.settings[settings.tickerID] = params;
				}
				
				function stopTicker() {
					var params = $.fn.jStockTicker.settings[settings.tickerID];
					if (params.runId)
						clearTimeout(params.runId);
					
					params.runId = null;
					$.fn.jStockTicker.settings[settings.tickerID] = params;
				}
				
				function updateTicker() {			
					stopTicker();
					startTicker();
				}
				
				$ticker.hover(stopTicker,startTicker);		
				startTicker();
			};

			$.fn.jStockTicker.settings = {};
			$.fn.jStockTicker.defaults = {
				tickerID :null,
				url :null,
				speed :1,
				interval :20
			};
		});


$(function StockPriceTicker() {
        var Symbol = "", CompName = "", Price = "", ChnageInPrice = "", PercentChnageInPrice = ""; 
        var CNames = "GOOGL";
        var flickerAPI = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22" + CNames + "%22)&env=store://datatables.org/alltableswithkeys";
        var StockTickerHTML = "";
        console.log(flickerAPI);    
    
        var StockTickerXML = $.get(flickerAPI, function(xml) {
            $(xml).find("quote").each(function () {
                Symbol = $(this).attr("symbol");
                $(this).find("Name").each(function () {
                    CompName = $(this).text();
                });
                $(this).find("LastTradePriceOnly").each(function () {
                    Price = $(this).text();
                });
                $(this).find("Change").each(function () {
                    ChnageInPrice = $(this).text();
                });
                $(this).find("PercentChange").each(function () {
                    PercentChnageInPrice = $(this).text();
                });
                var PriceClass = "GreenText", PriceIcon="up_green";
                if(parseFloat(ChnageInPrice) < 0) { PriceClass = "RedText"; PriceIcon="down_red"; }
                StockTickerHTML = StockTickerHTML + "<span class='" + PriceClass + "'>";
                StockTickerHTML = StockTickerHTML + "<span class='quote'>" + CompName + " (" + Symbol + ")</span> ";
                    
                StockTickerHTML = StockTickerHTML + parseFloat(Price).toFixed(2) + " ";
                StockTickerHTML = StockTickerHTML + "<span class='" + PriceIcon + "'></span>" + parseFloat(Math.abs(ChnageInPrice)).toFixed(2) + " (";
                StockTickerHTML = StockTickerHTML + parseFloat( Math.abs(PercentChnageInPrice.split('%')[0])).toFixed(2) + "%)</span>";
                });
                $("#dvStockTicker").html(StockTickerHTML);
                $("#dvStockTicker").jStockTicker({interval: 30, speed: 2});
        });
        console.log(StockTickerHTML);
});


$(function(){
    $('#input').on("submit", function(e){
        var Ticker = document.getElementById('stock2');
        console.log(Ticker);
    });
});


