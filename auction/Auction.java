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
    
    //Describes whether the auction is open or not (true if the Auction is open, otherwise false).
    private boolean isOpen;

    /**
     * Create a new auction.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        isOpen = true;
    }
    
    /**
     * Add a second constructor here.  This constructor takes
     * an Auction as a parameter.  Provided the auction parameter
     * is closed, the constructor creates a new auction containing
     * the unsold lots of the closed auction.  If the auction parameter
     * is still open or null, this constructor behaves like the
     * default constructor.
     * 
     * @param auction An auction object that is used to create a new auction by taking the unsold lots
     */
    
    public Auction(Auction auction){
        ArrayList newLots = new ArrayList<Lot>();
        if (auction.isOpen){
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
            isOpen = true ;
        } else {
            lots = auction.getNoBids();
            nextLotNumber = auction.nextLotNumber;
            isOpen = true;
        }
    }


    /**
     * Enter a new lot into the auction. Returns false if the
     * auction is not open or if the description is null.
     *
     * @param description A description of the lot.
     * 
     * @return True if a new lot was entered into the auction, false otherwise.
     */
    public boolean enterLot(String description)
    {
        if ((this.isOpen) && (description != null)){
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
     * If there are no lots, print a message indicating this.
     * 
     */
    public void showLots()
    {
        System.out.println();
        if (lots.isEmpty()){
            System.out.println("There are no lots currently being used.");
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
     * 
     * @return True if the bid was successful, false otherwise.
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {
        if (!(this.isOpen) || (lotNumber >= nextLotNumber) || (bidder == null) || (value <= 0)){
            return false;
        } else {
            System.out.println();
            Lot current = getLot(lotNumber);
            Bid newBid = new Bid(bidder, value);
            boolean isSuccessful = current.bidFor(newBid);
            System.out.println((isSuccessful) ? "Bid Successful." : "Bid unsuccessful.");
            if (isSuccessful){
                System.out.println(current.toString() + "    Bidder Name: " + bidder.getName());
                return true;
            } else {
                System.out.println(current.toString());
                return false;
            }
        }
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
        for (Lot lot : lots) {
            if (lot.getNumber() == lotNumber){
                return lot;
            }
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
     * 
     * @return False is the auction is closed, true otherwise. 
     */
    public boolean close()
    {
        if (this.isOpen) {
            for (Lot lot : lots) {
                System.out.println();
                if(lot.getHighestBid() != null){
                    System.out.println(lot.toString() + "    Bidder: " + lot.getHighestBid().getBidder().getName());
                } else {
                    System.out.println(lot.getNumber() + ": " + lot.getDescription() + ". This lot did not sell.");
                }
            }
            this.isOpen = false;
            return true;
        }
        return false;
    }
    
    /**
     * Returns an ArrayList containing all the items that have no bids so far.
     * (or have not sold if the auction has ended).
     * 
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
        ArrayList<Lot> noBids = new ArrayList<Lot>();
        for (Lot lot : lots) {
            if (lot.getHighestBid() == null){
                noBids.add(lot);
            }
        }
        return noBids;
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
     *
     * @param number The number of the lot to be removed. 
     * 
     * @return True if the lot was successfully removed, false otherwise.
     */
    public boolean removeLot(int number)
    {
        Iterator<Lot> it = lots.iterator();
        if (this.isOpen) {
            while (it.hasNext()){
                Lot lot = it.next();
                if ((lot.getNumber() == number) && (lot.getHighestBid() == null)){
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }
          
}
