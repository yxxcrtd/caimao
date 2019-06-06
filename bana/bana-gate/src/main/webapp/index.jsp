<html>

<script type="text/javascript" src="http://localhost:8080/caimao-0.0.1-SNAPSHOT/js/sockjs-0.3.min.js"></script>
        <script>
            var websocket;
            if ('WebSocket' in window) {
                websocket = new WebSocket("ws://localhost:8080/caimao-0.0.1-SNAPSHOT/webSocketServer");
            } else if ('MozWebSocket' in window) {
                websocket = new MozWebSocket("ws://localhost:8080/caimao-0.0.1-SNAPSHOT/webSocketServer");
            } else {
                websocket = new SockJS("http://localhost:8080/caimao-0.0.1-SNAPSHOT/sockjs/webSocketServer");
            }
            alert(websocket);
            websocket.onopen = function (evnt) {
            };
            websocket.onmessage = function (evnt) {
                alert(evnt.data);
            };
            websocket.onerror = function (evnt) {
            };
            window.onbeforeunload = function() {   
            	websocket.close();
            }; 
 
        </script>

<body>
	<h2>BitVC Trade Api!</h2>
	<p id="msgcount"></p>
</body>
</html>
