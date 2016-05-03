package gui.windows.main.datamodels;

import irc.IrcBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;

/**
 * Datamodel for network / channel list in GUI.
 * @author Kulttuuri
 */
public class NetworkChannelListDatamodel extends AbstractListModel {
    private List<IrcBuffer> channelsInView = new ArrayList<IrcBuffer>();
    private ChannelComparator channelComparer = new ChannelComparator();
    
    private class ChannelComparator implements Comparator<IrcBuffer> {
        @Override
        public int compare(IrcBuffer t1, IrcBuffer t2) {
            return (t1.getBufferName()).compareToIgnoreCase(t2.getBufferName());
        }
    }
    
    public IrcBuffer getChannelInViewIndex(int index) {
        return channelsInView.get(index);
    }
    
    public void addChannelToView(IrcBuffer channel) {
        int index = channelsInView.size();
        this.channelsInView.add(channel);
        sortChannels();
        fireIntervalAdded(this, index, index+1);
    }
    
    public void addChannelsToView(List<IrcBuffer> channels) {
        int startIndex = channelsInView.size();
        this.channelsInView.addAll(channels);
        sortChannels();
        fireIntervalAdded(this, startIndex, startIndex+channels.size());
    }
    
    // TODO: test.
    public void removeAllChannelsFromView() {
        int oldChanSize = channelsInView.size();
        this.channelsInView.clear();
        fireIntervalRemoved(this, 0, oldChanSize);
    }
    
    // TODO: test.
    public void removeChannelFromView(IrcBuffer channel) {
        // Find channel's index
        int index = 0;
        for (int i = 0; i < channelsInView.size(); i++)
        {
            if (channelsInView.get(i) == channel) {
                index = i;
            }
        }
        
        this.channelsInView.remove(channel);
        fireIntervalRemoved(this, index, index);
    }
    
    public void archiveChannelInView(IrcBuffer channel){
        for (IrcBuffer c : channelsInView){
            if (c == channel){
                c.setArchived(true);
                break;
            }
        }
        sortChannels();
        int index = 0;
        for (int i = 0; i < channelsInView.size(); i++) {
            if (channelsInView.get(i) == channel) {
                index = i;
                break;
            }
        }
        fireContentsChanged(this, index, index);           
    }
    
    public void unarchiveChannelInView(IrcBuffer channel){
        for (IrcBuffer c : channelsInView){
            if (c == channel){
                c.setArchived(false);
                break;
            }
        }
        sortChannels();
        int index = 0;
        for (int i = 0; i < channelsInView.size(); i++) {
            if (channelsInView.get(i) == channel) {
                index = i;
                break;
            }
        }
        fireContentsChanged(this, index, index);      
    }
    
    private class NetworkStructure {
	public IrcBuffer network;
        public List<IrcBuffer> channels = new ArrayList<IrcBuffer>();
        public List<IrcBuffer> queries = new ArrayList<IrcBuffer>();
        public List<IrcBuffer> archivedchannels = new ArrayList<IrcBuffer>();
        public List<IrcBuffer> archivedqueries = new ArrayList<IrcBuffer>();
	
	public NetworkStructure(IrcBuffer network) {
	    this.network = network;
	}
    }
    
    public void sortChannels() {
	try {
	    HashMap<String, NetworkStructure> statusChannels = new HashMap<String, NetworkStructure>();

	    // Find network status channels
	    for (IrcBuffer c : channelsInView) {
		if (c.isStatusChannel()) {
		    statusChannels.put(c.cid, new NetworkStructure(c));
		}
	    }

	    NetworkStructure network;
	    // Find channels and queries for networks
	    for (IrcBuffer c : channelsInView) {
		network = statusChannels.get(c.cid);
		// If network for this channel has not been initialized (for ex. in login), skip this.
		if (network == null) continue;
		// Skip status channels
		if (c.isStatusChannel()) continue;

		if (c.isQueryChannel()) {
		    if (c.isArchived()) {
			network.archivedqueries.add(c);
		    }
		    else {
			network.queries.add(c);
		    }

		}
		else {
		    if (c.isArchived()) {
			network.archivedchannels.add(c);
		    }
		    else {
			network.channels.add(c);
		    }

		}
	    }

	    // Sort all channels and queries
	    for (Map.Entry<String, NetworkStructure> entry : statusChannels.entrySet()) {
		network = entry.getValue();
		Collections.sort(network.channels, channelComparer);
		Collections.sort(network.queries, channelComparer);
		Collections.sort(network.archivedchannels, channelComparer);
		Collections.sort(network.archivedqueries, channelComparer);
	    }

	    // Clear old display list and add all the data back there
	    channelsInView.clear();
	    for (Map.Entry<String, NetworkStructure> entry : statusChannels.entrySet()) {
		network = entry.getValue();
		channelsInView.add(network.network);
		channelsInView.addAll(network.channels);
		channelsInView.addAll(network.queries);
		channelsInView.addAll(network.archivedchannels);
		channelsInView.addAll(network.archivedqueries);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    @Override
    public int getSize() {
        return this.channelsInView.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.channelsInView.get(index);
    }
    
}