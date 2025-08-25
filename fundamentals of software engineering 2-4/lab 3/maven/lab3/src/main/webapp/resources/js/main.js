function buttonClick(event) {
    const xElement = document.querySelector('input[name="j_idt10:j_idt12"]:checked');
    const y = document.getElementById('j_idt10:Y').value;
    const R = document.querySelector('select').value;

    if (validateX(xElement) && validateY(y) && validateR(R)) {
        notice("");
        console.log("Валидатор прошёл успешно!")
    } else {
        event.preventDefault()
        console.log("Отменила перенаправление")
    }
}

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

window.addEventListener('load', () => {
    const notification = document.getElementById("notification");
    if (notification && notification.textContent.trim() !== "") {
        notification.style.display = "block";
    }
})

function redrawGraph() {
    const graph = document.querySelector("#graph svg");

    graph.querySelectorAll(".littleDot").forEach(dot => {
        const x = dot.getAttribute("data-x");
        const y = dot.getAttribute("data-y");
        drawDot(x, y);
        dot.remove();
    });
}

function graphClick(){
    const R = document.querySelector('select').value
    if (validateR(R)) {
        const x = (event.offsetX - 200) / (75 / (R / 2));
        const y = (-event.offsetY + 200) / (75 / (R / 2));

        document.getElementById("graphForm:hiddenX").value = x;
        document.getElementById("graphForm:hiddenY").value = y;
        document.getElementById("graphForm:hiddenR").value = R;
        document.getElementById("graphForm:hiddenSubmitButton").click();
    } else {
        notice("Невозможно определить координату точки!");
    }
}

function drawDot(x, y) {
    const graph = document.querySelector("#graph svg");
    const R = document.querySelector('select').value;

    const cx = ((150 * Number(x)) / R + 200);
    const cy = (-(150 * Number(y)) / R + 200);

    const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttributeNS(null, "class", "littleDot");
    circle.setAttributeNS(null, "cx", cx.toString());
    circle.setAttributeNS(null, "cy", cy.toString());
    circle.setAttributeNS(null, "r", "5");
    circle.setAttributeNS(null, "fill", "red");
    if (isInArea(x, y, R)) circle.setAttributeNS(null, "fill", "green");
    circle.setAttributeNS(null, "stroke", "black");
    circle.setAttributeNS(null, "stroke-width", "1");
    circle.setAttributeNS(null, "opacity", "0.8");
    circle.setAttributeNS(null, "filter", "url(#shadow)");
    circle.setAttributeNS(null, "data-x", x.toString());
    circle.setAttributeNS(null, "data-y", y.toString());

    graph.appendChild(circle);
}

function updateGraph(data) {
    switch (data.status) {
        case "begin":
            break;
        case "complete":
            break;
        case "success":
            updateHeight()
            const point = JSON.parse((document.getElementById("lastPoint")).value);
            if (point.id !== -1) drawDot(point.x, point.y);
            break;
    }
}

function isInArea(x, y, r) {
    let firstCond = (x <= 0 && y >= 0 &&
        Math.pow(y, 2) <= Math.pow(r / 2, 2) - Math.pow(x, 2));
    let secondCond = (x <= 0 && y <= 0 && y >= -2 * x - r);
    let thirdCond = (x >= 0 && y <= 0 && x <= r / 2 && y >= -r);
    return firstCond || secondCond || thirdCond;
}
