let stompClient = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('/websocket-endpoint'));
    stompClient.connect({}, (frame) => {
        stompClient.subscribe('/topic/response',
            (greeting) => requestStatus(JSON.parse(greeting.body)));
        stompClient.subscribe('/topic/notification',
            (greeting) => serverNotificationCallback(JSON.parse(greeting.body)));
    });
};

function checkForm() {
    if (!$("#fio").val()) {
        return false;
    }
    if (!$("#age").val()) {
        return false;
    }
    return true;
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
};

const serverNotificationCallback = (data) => {
    console.log('data:' + data);
    if (data.type !== 'entity') {
        return;
    }
    $("#users").append("<tr>"
        + "<td>" + data.entity.id + "</td>"
        + "<td>" + data.entity.name + "</td>"
        + "<td>" + data.entity.age + "</td>"
        + "</tr>");
};

const requestStatus = (data) => {
    console.log(data)
};

function saveNewUser() {
    if (!checkForm()) {
        return;
    }
    let message = {
        requestId: uuidv4(),
        command: 'create',
        entity: 'user',
        content: {
            fio: $("#fio").val(),
            age: $("#age").val(),
        }
    };
    stompClient.send("/app/message", {}, JSON.stringify(message));
}

$(function () {
    connect();
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#save-user").click(saveNewUser);
});

window.onunload = function () {
    disconnect();
};

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}
