const cellHeight = window.innerHeight / 4;
let catTable;
let catTableRows = document.body.scrollHeight / cellHeight;

window.addEventListener('load', () => {
    initCatTable();
});

function initCatTable() {
    catTable = document.getElementsByClassName('cat-table')[0];
    const catDiv = document.getElementsByClassName('cat-container')[0];
    catDiv.style.height = document.body.scrollHeight + 'px';
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
    catImg.src = `resources/img/cat${randomCat}.png`;

    const randomWidth = getRandomSize(300, 400);
    catImg.style.width = `${randomWidth}px`;

    catImg.addEventListener('mouseover', () => {
        catImg.src = `resources/img/cat${randomCat}_yawn.png`;
    });

    catImg.addEventListener('mouseout', () => {
        catImg.src = `resources/img/cat${randomCat}.png`;
    });

    return catImg;
}

function createTable() {
    for (let r = 0; r < catTableRows; r++) {
        createRow();
    }
}

function updateHeight() {
    const catDiv = document.getElementsByClassName('cat-container')[0];
    catDiv.style.height = document.body.scrollHeight + 'px';
    if (document.body.scrollHeight - catTable.offsetHeight >= cellHeight) {
        catTableRows++;
        createRow();
    }
}