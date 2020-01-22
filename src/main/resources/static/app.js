let stompClient = null
let scatterChart

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
    stompClient.debug = null
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
        animation: {
            duration: 0
        },
        hover: {
            animationDuration: 0
        },
        responsiveAnimationDuration: 0
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
    // let numToDel = scatterChart.data.datasets[0].data.length - 2000
    // if(numToDel > 0) {
    //     scatterChart.data.datasets[0].data.splice(0,numToDel);
        // scatterChart.data.datasets[0].data = []
    // }
    Array.prototype.push.apply(scatterChart.data.datasets[0].data, packets)
    scatterChart.update()
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}