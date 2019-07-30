public class ScreenLock {

    public int calculateCombinations(char startPosition, int patternLength){
        if (patternLength < 1 || patternLength > 9)
            return 0;
        if (patternLength == 1) {
            return 1;
        }
        boolean[][] usedDotsMatrix = new boolean[3][3];  //indicates used dots (true - used, false - not used)
        return countCombinations(startPosition, patternLength - 1, usedDotsMatrix);
    }

    private int countCombinations(char dotName, int depth, boolean[][] usedDotsMatrix) {
        boolean[][] matrix = new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(usedDotsMatrix[i], 0, matrix[i], 0, 3);
        }
        Dot dot;
        try {
            dot = new Dot(dotName, matrix);
        } catch (NotValidDotException e) {
            e.printStackTrace();
            return 0;
        }
        char[] nextDots = dot.getPossibleNextDots();
        if (depth == 1) {
            return nextDots.length;
        }
        int combinations = 0;
        matrix[dot.getX() - 1][dot.getY() - 1] = true;
        while (depth-- > 1) {
            for (char ch : nextDots) {
                combinations += countCombinations(ch, depth, matrix);
            }
            depth = 1;
        }
        return combinations;
    }


    class Dot {
        private char name;
        private char[] possibleNextDots;
        private int x;
        private int y;
        private boolean[][] usedDotsMatrix;
        private char[][] nameMatrix = {{'A', 'B', 'C'},
                {'D', 'E', 'F'},
                {'G', 'H', 'I'}};

        public Dot(char name, boolean[][] usedDotsMatrix) throws NotValidDotException {
            this.usedDotsMatrix = usedDotsMatrix;
            this.name = name;
            int coordinates = findCoordinates(name);
            if (coordinates == 0)
                throw new NotValidDotException("Dot with name '" + name + "' not in the screen lock matrix");
            x = coordinates / 10;
            y = coordinates % 10;
            possibleNextDots = findPossibleNextDots(coordinates);
        }

        private char[] findPossibleNextDots(int coordinates) {
            StringBuilder sb = new StringBuilder();
            int xInversion = 1;
            int yInversion = 1;
            switch (coordinates) {
                case 22:
                    for (int i = 1; i <= 3; i++) {
                        for (int j = 1; j <= 3; j++) {
                            if (i == 2 && j == 2)
                                continue;
                            appendDotIfNotUsed(sb, i, j);
                        }
                    }
                    break;
                case 11:
                    appendForCornerDots(sb, x, y, xInversion, yInversion);
                    break;
                case 13:
                    yInversion = -1;
                    appendForCornerDots(sb, x, y, xInversion, yInversion);
                    break;
                case 31:
                    xInversion = -1;
                    appendForCornerDots(sb, x, y, xInversion, yInversion);
                    break;
                case 33:
                    xInversion = -1;
                    yInversion = -1;
                    appendForCornerDots(sb, x, y, xInversion, yInversion);
                    break;
                case 12:
                    appendForSideDots(sb, true, xInversion, yInversion);
                    break;
                case 21:
                    appendForSideDots(sb, false, xInversion, yInversion);
                    break;
                case 32:
                    xInversion = -1;
                    appendForSideDots(sb, true, xInversion, yInversion);
                    break;
                case 23:
                    yInversion = -1;
                    appendForSideDots(sb, false, xInversion, yInversion);
                    break;
            }
            return sb.toString().toCharArray();
        }

        private void appendForSideDots(StringBuilder stringBuilder, boolean centerLineHorizontal, int xInversion, int yInversion) {
            for (int i = 1; i <= 3; i++) {
                if (centerLineHorizontal) {
                    appendDotIfNotUsed(stringBuilder, i, 1);
                    appendDotIfNotUsed(stringBuilder, i, 3);
                } else {
                    appendDotIfNotUsed(stringBuilder, 1, i);
                    appendDotIfNotUsed(stringBuilder, 3, i);
                }
            }
            if (centerLineHorizontal) {
                if (!appendDotIfNotUsed(stringBuilder, 2, 2))
                    appendDotIfNotUsed(stringBuilder, 2 + xInversion, 2);
            } else {
                if (!appendDotIfNotUsed(stringBuilder, 2, 2))
                    appendDotIfNotUsed(stringBuilder, 2, 2 + yInversion);
            }
        }

        private void appendForCornerDots(StringBuilder stringBuilder, int x, int y, int xInversion, int yInversion) {
            appendDotIfNotUsed(stringBuilder, x + xInversion, y + yInversion * 2);
            appendDotIfNotUsed(stringBuilder, x + xInversion * 2, y + yInversion);
            if (!appendDotIfNotUsed(stringBuilder, x + xInversion, y))
                appendDotIfNotUsed(stringBuilder, x + xInversion * 2, y);
            if (!appendDotIfNotUsed(stringBuilder, x, y + yInversion))
                appendDotIfNotUsed(stringBuilder, x, y + yInversion * 2);
            if (!appendDotIfNotUsed(stringBuilder, x + xInversion, y + yInversion))
                appendDotIfNotUsed(stringBuilder, x + xInversion * 2, y + yInversion * 2);
        }

        private boolean appendDotIfNotUsed(StringBuilder stringBuilder, int x, int y) {
            if (usedDotsMatrix[x - 1][y - 1])
                return false;
            stringBuilder.append(nameMatrix[y - 1][x - 1]);
            return true;
        }

        public char[] getPossibleNextDots() {
            return possibleNextDots;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "" + name;
        }

        // returns 2-digit number, where first digit is coordinate x, and second digit is coordinate y;
        // returns 0 if given character is not in the screen lock usedDotsMatrix [A-I];
        private int findCoordinates(char c) {
            int x;
            int y;
            switch (c) {
                case 'A':
                    y = 1;
                    x = 1;
                    break;
                case 'D':
                    y = 2;
                    x = 1;
                    break;
                case 'G':
                    y = 3;
                    x = 1;
                    break;
                case 'B':
                    y = 1;
                    x = 2;
                    break;
                case 'E':
                    y = 2;
                    x = 2;
                    break;
                case 'H':
                    y = 3;
                    x = 2;
                    break;
                case 'C':
                    y = 1;
                    x = 3;
                    break;
                case 'F':
                    y = 2;
                    x = 3;
                    break;
                case 'I':
                    y = 3;
                    x = 3;
                    break;
                default:
                    x = 0;
                    y = 0;
            }
            return x * 10 + y;
        }
    }

    class NotValidDotException extends Exception {
        public NotValidDotException(String message) {
            super(message);
        }
    }
}
