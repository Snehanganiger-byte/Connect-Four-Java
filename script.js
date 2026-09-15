const ROWS = 6;
const COLS = 7;

let board = [];
let currentPlayer = 1;

let score1 = 0;
let score2 = 0;

let gameOver = false;

const boardElement = document.getElementById("board");
const buttonsElement = document.getElementById("column-buttons");
const statusElement = document.getElementById("status");

const score1Element = document.getElementById("score1");
const score2Element = document.getElementById("score2");

const newGameButton = document.getElementById("new-game");

function createGame() {

    board = [];

    for (let row = 0; row < ROWS; row++) {

        board[row] = [];

        for (let col = 0; col < COLS; col++) {
            board[row][col] = 0;
        }
    }

    currentPlayer = 1;
    gameOver = false;

    createColumnButtons();
    renderBoard();

    statusElement.textContent = "Player 1's Turn";
}

function createColumnButtons() {

    buttonsElement.innerHTML = "";

    for (let col = 0; col < COLS; col++) {

        const button = document.createElement("button");

        button.textContent = "↓";

        button.addEventListener("click", function () {
            dropDisc(col);
        });

        buttonsElement.appendChild(button);
    }
}

function renderBoard() {

    boardElement.innerHTML = "";

    for (let row = 0; row < ROWS; row++) {

        for (let col = 0; col < COLS; col++) {

            const cell = document.createElement("div");

            cell.classList.add("cell");

            if (board[row][col] === 1) {
                cell.classList.add("red");
            }

            if (board[row][col] === 2) {
                cell.classList.add("yellow");
            }

            cell.addEventListener("click", function () {
                dropDisc(col);
            });

            boardElement.appendChild(cell);
        }
    }
}

function dropDisc(col) {

    if (gameOver) {
        return;
    }

    let row = -1;

    for (let r = ROWS - 1; r >= 0; r--) {

        if (board[r][col] === 0) {
            row = r;
            break;
        }
    }

    if (row === -1) {
        statusElement.textContent = "Column is full!";
        return;
    }

    board[row][col] = currentPlayer;

    renderBoard();

    if (checkWin(row, col)) {

        gameOver = true;

        highlightWinningCells(row, col);

        if (currentPlayer === 1) {
            score1++;
            score1Element.textContent = score1;
        } else {
            score2++;
            score2Element.textContent = score2;
        }

        statusElement.textContent =
            `🎉 Player ${currentPlayer} Wins!`;

        return;
    }

    if (checkDraw()) {

        gameOver = true;

        statusElement.textContent = "It's a Draw!";

        return;
    }

    currentPlayer = currentPlayer === 1 ? 2 : 1;

    statusElement.textContent =
        `Player ${currentPlayer}'s Turn`;
}

function checkWin(row, col) {

    const player = board[row][col];

    const directions = [
        [0, 1],
        [1, 0],
        [1, 1],
        [1, -1]
    ];

    for (const [rowDirection, colDirection] of directions) {

        let count = 1;

        count += countDirection(
            row,
            col,
            rowDirection,
            colDirection,
            player
        );

        count += countDirection(
            row,
            col,
            -rowDirection,
            -colDirection,
            player
        );

        if (count >= 4) {
            return true;
        }
    }

    return false;
}

function countDirection(
    row,
    col,
    rowDirection,
    colDirection,
    player
) {

    let count = 0;

    let r = row + rowDirection;
    let c = col + colDirection;

    while (
        r >= 0 &&
        r < ROWS &&
        c >= 0 &&
        c < COLS &&
        board[r][c] === player
    ) {

        count++;

        r += rowDirection;
        c += colDirection;
    }

    return count;
}

function checkDraw() {

    for (let col = 0; col < COLS; col++) {

        if (board[0][col] === 0) {
            return false;
        }
    }

    return true;
}

function highlightWinningCells(row, col) {

    const player = board[row][col];

    const directions = [
        [0, 1],
        [1, 0],
        [1, 1],
        [1, -1]
    ];

    let winningCells = [];

    for (const [dr, dc] of directions) {

        let cells = [[row, col]];

        let r = row + dr;
        let c = col + dc;

        while (
            r >= 0 &&
            r < ROWS &&
            c >= 0 &&
            c < COLS &&
            board[r][c] === player
        ) {

            cells.push([r, c]);

            r += dr;
            c += dc;
        }

        r = row - dr;
        c = col - dc;

        while (
            r >= 0 &&
            r < ROWS &&
            c >= 0 &&
            c < COLS &&
            board[r][c] === player
        ) {

            cells.push([r, c]);

            r -= dr;
            c -= dc;
        }

        if (cells.length >= 4) {

            winningCells = cells;
            break;
        }
    }

    const cellElements = document.querySelectorAll(".cell");

    winningCells.forEach(([r, c]) => {

        const index = r * COLS + c;

        if (cellElements[index]) {
            cellElements[index].classList.add("winner");
        }
    });
}

newGameButton.addEventListener("click", function () {

    createGame();

});

createGame();
