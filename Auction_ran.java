import java.util.*;

/**
 * A simple model of an auction.
 * The auction maintains a list of lots of arbitrary length.
 *
 * @author David J. Barnes and Michael Kolling.
 * @version 2006.03.30
 *
 * @author (of AuctionSkeleton) Lynn Marshall
 * @version 2.0
 * 
 * @author Riya Arora 101190033
 * @version 2022.02.09
 * 
 */
public class Auction
{
    /** The list of Lots in this auction. */
    private ArrayList<Lot> lots;

    /** 
     * The number that will be given to the next lot entered
     * into this auction.  Every lot gets a new number, even if some lots have
     * been removed.  (For example, if lot number 3 has been deleted, we don't
     * reuse it.)
     */
    private int nextLotNumber;
    private boolean isItOn; //Boolean variable to check if the auction is on or not

    /**
     * Create a new auction.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        isItOn = true; //Auction starts when it is enabled
    }
    
    /**
     * Add a second constructor here.  This constructor takes
     * an Auction as a parameter.  Provided the auction parameter
     * is closed, the constructor creates a new auction containing
     * the unsold lots of the closed auction.  If the auction parameter
     * is still open or null, this constructor behaves like the
     * default constructor.
     * (You need to write the entire method.)
     * @param auction is an Auction object that's used to make a new auction using unsold lots.
     */
    public Auction (Auction auction){
        if (auction.isItOn || auction == null){
            lots = new ArrayList <Lot>();
            nextLotNumber = 1;
        }
        else {
            lots = auction.getNoBids();
            nextLotNumber = auction.nextLotNumber;
        }
        isItOn = true;
    }
    /**
     * Enter a new lot into the auction.  Returns false if the
     * auction is not open or if the description is null.
     * (You need to add the return type, update the documentation, 
     * and change the code.)
     *
     * @param description A description of the lot.
     * @return false is the auction is not active/open or if description
     * null/empty.
     * @return true if user was able to input a new lot value into the auction.
     */
    public boolean enterLot(String description)
    {
        if(isItOn == true && (description != null)){
            lots.add(new Lot(nextLotNumber, description));
            nextLotNumber++;
            return true;
        }
        return false;
    }
    
    /**
     * Show the full list of lots in this auction.
     *
     * Print a blank line first, to make our output look nicer. 
     * If there are no lots, print a message indicating this 
     * (You need to update the code given below.)
     */
    public void showLots()
    {
        System.out.println();
        if(lots.isEmpty()){
            System.out.println("There are no lots in this auction.");
        }
        for(Lot lot : lots) {
            System.out.println(lot.toString());
        }
    }
    
    /**
     * Bid for a lot.
     * Do not assume that the lots are stored in numerical order.
     * Prints a message indicating whether the bid is successful or not.
     *   
     * First print a blank line.  
     * Then print whether or not the bid is successful.
     * If the bid is successful, also print the
     * lot number, high bidder's name, and the bid value.
     * If the bid is not successful, also print the lot number 
     * and high bid (but not the high bidder's name).
     * 
     * Returns false if the auction is closed, the lot doesn't
     * exist, the bidder is null, or the bid was not positive
     * and true otherwise (even if the bid was not high enough).
     * (You need to update the return type, documentation, and code.)
     *
     * @param number The lot number being bid for.
     * @param bidder The person bidding for the lot.
     * @param value  The value of the bid.
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {
          Lot chosenLot = getLot(lotNumber);
          if(isItOn != true || bidder.equals(null) || value <= 0 || chosenLot == null){ //Setting up the restrictions
            return false;    
          }
          System.out.println();
          Bid bid = new Bid(bidder, value);
          boolean successful = chosenLot.bidFor(bid);
          if (chosenLot != null){
              if(successful){
                  System.out.println("The bid for lot number " + lotNumber + " was successful."); 
                  System.out.println("The highest bidder is now " + chosenLot.getHighestBid().getBidder().getName()
                  + " with a bid of $" + chosenLot.getHighestBid().getValue());
                  return true;
              } else{
                  Bid highBid = chosenLot.getHighestBid();
                  System.out.println("The bid for lot number " + lotNumber + " was not successful." + "The value of highest bid was $" +
                  highBid.getValue());
                  return false;
              }
          }
          return true;
    }
    /**
     * Return the lot with the given number. 
     * Do not assume that the lots are stored in numerical order.   
     *   
     * Returns null if the lot does not exist.
     * (You need to update the code.)
     *
     * @param lotNumber The number of the lot to return.
     *
     * @return the Lot with the given number
     */
    public Lot getLot(int lotNumber)
    {
       for (Lot lot: lots){
           if(lot.getNumber () == lotNumber)
              return lot;
       }
       return null;
    }
    
    /**
     * Closes the auction and prints information on the lots.
     * First print a blank line.  Then for each lot,
     * its number and description are printed.
     * If it did sell, the high bidder and bid value are also printed.  
     * If it didn't sell, print that it didn't sell.
     *
     * Returns false if the auction is already closed, true otherwise.
     * (You need to update the return type, documentation, and code.)
     * @return false if the auction was already closed
     * @return true if the auction is currently on
     */ 
    public boolean close()
    {
        if (isItOn != true){
            return false;
        }
        isItOn = false;
        System.out.println();
        for (Lot lot: lots){
            System.out.println("The lot number is " + lot.getNumber() + " and it is for " + lot.getDescription() + "");
            if (lot.getHighestBid() != null){
                System.out.println("The highest bidder is " + lot.getHighestBid().getBidder().getName() + ". The bid value was $" +
                lot.getHighestBid().getValue());
            }
            if(lot.getHighestBid() == null){
                System.out.println("The item failed to sell");
            }
        }
        return true;
    }
    
    /**
     * Returns an ArrayList containing all the items that have no bids so far.
     * (or have not sold if the auction has ended).
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
       ArrayList<Lot> noBid = new ArrayList<Lot>();
       for (Lot lot: lots){
           if(lot.getHighestBid() == null){
               noBid.add(lot);
           }
       }
       return noBid;
    }
    /**
     * Remove the lot with the given lot number, as long as the lot has
     * no bids, and the auction is open.  
     * You must use an Iterator object to search the list and,
     * if applicable, remove the item.
     * Do not assume that the lots are stored in numerical order.
     *
     * Returns true if successful, false otherwise (auction closed,
     * lot does not exist, or lot has a bid).
     * (You need to update the return type, documentation, and code.)
     *
     * @param number The number of the lot to be removed.
     * @return true if removing the lot was successful
     * @return false if the lot DNE, auction was closed or lot has a current bid 
     * placed on it.
     */
    public boolean removeLot(int number)
    {
        Iterator<Lot> it = lots.iterator();
        if (isItOn == true){
            while(it.hasNext()){
                Lot l = it.next();
                if((l.getNumber() == number) && (l.getHighestBid()==null)){
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }
}