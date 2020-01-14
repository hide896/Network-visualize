let stompClient = null
let scatterChart

let startTime = 0
const threTime = 60 * 1000 // 1 min
let packetCount = 0

$(function () {
    drawChart()
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).on("click", connect())
    $( "#disconnect" ).on("click", disconnect())
    $( "#send" ).on("click", sendName())
})

function setConnected(connected) {
    $("#connect").prop("disabled", connected)
    $("#disconnect").prop("disabled", !connected)
    if (connected) {
        $("#conversation").show()
    }
    else {
        $("#conversation").hide()
    }
    $("#greetings").html("")
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, function (frame) {
        setConnected(true)
        console.log('Connected: ' + frame)
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content)
        })
        stompClient.subscribe('/topic/packets', function (packets) {
            addData(JSON.parse(packets.body))
        })
    })
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    setConnected(false)
    console.log("Disconnected")
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}))
}

function drawChart() {
    const ctx = document.getElementById('myChart').getContext('2d')
    const options = {
        scales: {
            xAxes: [{
                type: 'linear',
                position: 'bottom',
                ticks: {
                    beginAtZero: true,
                    max: 256
                }
            }],
            yAxes: [{
                ticks: {
                    beginAtZero: true,
                    max: 256
                }
            }]
        },
        pointRadius: 1,
        pointHoverRadius: 1,
        aspectRatio: 1,
        events: [],
        animation: false
    }

    scatterChart = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                label: 'パケットの分布',
                data: []
            }]
        },
        options: options
    })
}

function addData(packets) {
    if(startTime == 0) startTime = Date.now()
    packetCount += packets.length
    if(Date.now() - startTime > threTime) {
        console.log({packetCount})
        disconnect()
    }
    for(let i = 0; i < packets.length; ++i) {
        scatterChart.data.datasets[0].data.push({x: packets[i].x, y: packets[i].y})
    }
    scatterChart.update()
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}