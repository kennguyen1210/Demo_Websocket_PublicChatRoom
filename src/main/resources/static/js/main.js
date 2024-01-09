'use strict' // bat co che safe code , kiem tra do tin cay cua code

var messageForm = document.querySelector("#messageForm"); // the form
var messageInput = document.querySelector("#message"); // the input
var messageArea = document.querySelector("#messageArea"); // noi hien thi tin nhan
var connectingElement = document.querySelector("#connecting"); // loader

var stompClient = null; // bien dai dien ket noi
var username = null; // ten nguoi dung

// ham ket noi toi websocket
function connect(){
    username = document.querySelector("#username").innerText.trim(); // lay ra username tai login/logout

    var socket = new SockJS('/ws'); //  tao doi tuong socket;
    stompClient = Stomp.over(socket);
    stompClient.connect({},onConnected, onError); //  mo lien ket voi websocket
}
connect();

// khi da ket noi duoc
function onConnected(){
    // dang ky topic chat
    stompClient.subscribe('/topic/public-chat-room', onMessageReceived);

    // gui toi soc ket doi tuong chat voi thong tin la vao room
    stompClient.send("/app/chat/add-user",
        {},
        JSON.stringify({sender:username,type:'JOIN'})
    );
    connectingElement.classList.add('hidden'); //  an loader
}

// xu li loi ket noi server
function onError(error){
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// ham gui tin nhan
function sendMessage(event){
    var messageContext = messageInput.value.trim();// lay ra tin nhan tu input
    if(messageContext && stompClient){
        var chat = {
            sender : username,
            message: messageContext,
            type: 'CHAT'
        }
        stompClient.send("/app/chat/send-message",{},JSON.stringify(chat));
        messageInput.value= "";
    }
    event.preventDefault();
}

// ham nhan list tin nhan tu server
function onMessageReceived(payload){
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement("li");
    console.log("message",message);
    if(message.type === 'JOIN'){
        messageElement.classList.add('event-message');
        message.message = message.sender + " joined!";
    } else if(message.type === 'LEAVE'){
        messageElement.classList.add('event-message');
        message.message = message.sender + " left!";
    } else {
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('strong');
        usernameElement.classList.add('nickname');
        var usernameContext = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameContext);
        messageElement.appendChild(usernameElement)
    }
    var textElement = document.createElement('span');
    var messageText = document.createTextNode(message.message);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit',sendMessage, true);


