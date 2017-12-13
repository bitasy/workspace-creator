package authoring.buttons.strategies;

/**
 * The interface passed to CustomButton's that provides them
 * with their fire() functionality
 *
 * @author Albert Xue
 */
public interface IButtonStrategy {
    /**
     * The function to be fired off by a CustomButton when that button is pressed
     */
    void fire();
}