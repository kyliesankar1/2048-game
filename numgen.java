package org.cis1200.game2048;


public interface NumberGenerator {
    /**
     * Generates a number based of off a specified bound.
     *
     * @param bound - the max value that can be returned by the generator.
     * @return a number based off of the bound
     */
    int next(int bound);
}
