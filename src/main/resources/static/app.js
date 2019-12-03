let stompClient = null;
let scatterChart
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/packets', function (packet) {
            addData(JSON.parse(packet.body))
        })
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function drawChart() {
    const ctx = document.getElementById('myChart').getContext('2d');
    const options = {
        scales: {
            xAxes: [{
                type: 'linear',
                position: 'bottom'
            }]
        },
        aspectRatio: 1,
        events: [],
        animation: false
    }

    scatterChart = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                label: 'パケットの分布',
                data: [{x:208,y:137},{x:1,y:1},{x:250,y:255},{x:21,y:90}]
            }]
        },
        options: options
    });
}

function addData(packet) {
    scatterChart.data.datasets[0].data.push({x: packet.x, y: packet.y})
    scatterChart.update()
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    drawChart()
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

});