function updateClock() {
    var now = new Date();
    var hours = now.getHours().toString().padStart(2, '0');
    var minutes = now.getMinutes().toString().padStart(2, '0');
    var seconds = now.getSeconds().toString().padStart(2, '0');
    var date = now.getDate().toString().padStart(2, '0');
    var month = (now.getMonth() + 1).toString().padStart(2, '0');
    var year = now.getFullYear();

    var timeString = date + '.' + month + '.' + year + ' ' + hours + ':' + minutes + ':' + seconds;

    document.getElementById('clock').textContent = timeString;
    console.log("Clock updated:", timeString); // Для отладки
}

setInterval(updateClock, 6000);

window.addEventListener('load', () => {
    updateClock();
});