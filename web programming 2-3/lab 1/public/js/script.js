const cellHeight = window.innerHeight / 4;
let catTable;
let catTableRows = 4;

document.getElementById("checkButton").addEventListener("click", function () {
    const xElement = document.querySelector('input[name="X"]:checked');
    const y = document.getElementById("Y").value.trim();
    const R = document.querySelector('select').value;

    if (validateX(xElement) && validateY(y) && validateR(R)) {
        console.log("Валидатор прошел успешно");
        notice("");
        sendRequest(xElement.value, y, R);
    }
});

function validateX(xElement) {
    if (!xElement) {
        notice("Выберите значение X");
        return false;
    }
    return true;
}

function validateY(y) {
    const numberY = Number(y.replace(',', '.'));
    if (!y) {
        notice("Введите значение Y");
        return false;
    } else if (isNaN(numberY)) {
        notice("Значение Y должно быть числом");
        return false;
    } else if (!(numberY >= -5 && numberY <= 3)) {
        notice("Значение Y должно быть в диапазоне от -5 до 3");
        return false;
    }
    return true;
}

function validateR(R) {
    if (!R) {
        notice("Выберите значение R");
        return false;
    }
    return true;
}

function notice(message) {
    const notification = document.getElementById("notification");
    if (!message) {
        notification.style.display = "none";
        return;
    }
    notification.innerText = message;
    notification.style.display = "block";
}

function sendRequest(x, y, R) {
    superagent
        .get("/fcgi-bin/server.jar")
        .query({ x: x, y: y, R: R })
        .then(response => {
            console.log("Ответ от сервера:", response.body);
            notice("");
            addTableRow(x, y, R, response.body.isInArea, response.body.time);
            updateHeight();
        })
        .catch(error => {
            notice(error.response.body.message);
            console.error("Ошибка при отправке данных:", error);
        });
}

function addTableRow(x, y, R, isInArea, time) {
    const table = document.getElementById("table").getElementsByTagName("tbody")[0];
    const newRow = table.insertRow();
    const cellX = newRow.insertCell(0);
    const cellY = newRow.insertCell(1);
    const cellR = newRow.insertCell(2);
    const cellResult = newRow.insertCell(3);
    const cellCurrentTime = newRow.insertCell(4);
    const cellRunningTime = newRow.insertCell(5);

    cellX.textContent = x;
    cellY.textContent = y;
    cellR.textContent = R;
    cellResult.textContent = isInArea;
    cellCurrentTime.textContent = new Date().toLocaleTimeString();
    cellRunningTime.textContent = time;
}

function updateHeight() {
    const catDiv = document.getElementsByClassName('cat-container')[0];
    catDiv.style.height = document.body.scrollHeight + 'px';
    if (document.body.scrollHeight - catTable.offsetHeight >= cellHeight) {
        catTableRows++;
        createRow();
    }
}

window.onload = () => {
    initCatTable();
}

function initCatTable() {
    catTable = document.getElementsByClassName('cat-table')[0];
    createTable();
}

function getRandomSize(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function createRow() {
    const row = document.createElement('tr');
    for (let c = 0; c < 6; c++) {
        const cell = document.createElement('td');
        const catImg = createCatImage();
        cell.style.height = cellHeight + 'px';
        cell.appendChild(catImg);
        row.appendChild(cell);
    }
    catTable.appendChild(row);
}

function createCatImage() {
    const catImg = document.createElement('img');
    catImg.classList.add('cat');
    const randomCat = Math.floor(Math.random() * 28) + 1;
    catImg.src = `img/cat${randomCat}.png`;

    const randomWidth = getRandomSize(300, 400);
    catImg.style.width = `${randomWidth}px`;

    catImg.addEventListener('mouseover', () => {
        catImg.src = `img/cat${randomCat}_yawn.png`;
    });

    catImg.addEventListener('mouseout', () => {
        catImg.src = `img/cat${randomCat}.png`;
    });

    return catImg;
}

function createTable() {
    for (let r = 0; r < catTableRows; r++) {
        createRow();
    }
}
