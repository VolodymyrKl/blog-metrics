"use strict";
exports.__esModule = true;
var react_1 = require("react");
var sockjs_client_1 = require("sockjs-client");
var stompjs_1 = require("@stomp/stompjs");
var WordCountDisplayComponent = function () {
    var _a = react_1.useState(), messages = _a[0], setMessages = _a[1];
    var stompClientRef = react_1["default"].useRef(null);
    var stompClient = new stompjs_1.Client({
        webSocketFactory: function () { return new sockjs_client_1["default"]('http://localhost:8080/ws'); },
        reconnectDelay: 500,
    });
    react_1.useEffect(function () {
        stompClient.onConnect = function (frame) {
            console.log('Connected: ' + frame);
            requestPostUpdates();
            stompClient.subscribe('/api/word-count', function (message) {
                var content = JSON.parse(message.body);
                setMessages(content);
            });
        };
        stompClient.onWebSocketError = function (error) {
            console.error('Error with websocket', error);
        };
        stompClient.onStompError = function (frame) {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };
        stompClient.activate();
        stompClientRef.current = stompClient;
        return function () {
            console.log('Component unmounted, but WebSocket remains active.');
        };
    }, []);
    var requestPostUpdates = function () {
        if (stompClient.connected) {
            stompClient.publish({
                destination: "/app/check-updates",
            });
        }
    };
    return (react_1["default"].createElement("div", null,
        react_1["default"].createElement("h1", null, "WebSocket Messages"),
        react_1["default"].createElement("table", { id: "word-count" },
            react_1["default"].createElement("thead", null,
                react_1["default"].createElement("tr", null,
                    react_1["default"].createElement("th", null, "Message"))),
            react_1["default"].createElement("tbody", null, messages && Object.entries(messages).map(function (_a) {
                var word = _a[0], count = _a[1];
                return (react_1["default"].createElement("tr", { key: word },
                    react_1["default"].createElement("td", null,
                        word,
                        ": ",
                        count)));
            })))));
};
exports["default"] = WordCountDisplayComponent;
