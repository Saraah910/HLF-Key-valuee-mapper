/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.List;


import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(
        name = "basic",
        info = @Info(
                title = "Asset Transfer",
                description = "The hyperlegendary asset transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class AssetTransfer implements ContractInterface {

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    /**
     * Creates some initial assets on the ledger.
     *
     * @param ctx the transaction context
     */
    public static class Asset {
        public String AssetID;
        public String Name;
        public String Color;
        public int Size;
    }

    public static class NewOwner {
        public String OwnerID;
        public String OwnerName;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)

    public void InitLedger(final Context ctx){
        ChaincodeStub stub = ctx.getStub();

        mappingOwnerAsset(ctx, "1001", "ASSET001")
        mappingOwnerAsset(ctx, "1002", "ASSET002")
        mappingOwnerAsset(ctx, "1003", "ASSET003")
        mappingOwnerAsset(ctx, "1004", "ASSET004")
        mappingOwnerAsset(ctx, "1005", "ASSET005")
    }

    /**
     * Creates a new asset on the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the new asset
     * @param color the color of the new asset
     * @param size the size for the new asset
     * @return the created asset
     */
     

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String CreateOwner(Context ctx, String ownerId, String ownerName) {
        ChaincodeStub stub = ctx.getStub();
        NewOwner newOwner = new NewOwner();
        newOwner.OwnerID = ownerId;
        newOwner.OwnerName = ownerName;

        String sortedJson = genson.serialize(newOwner);
        return ownerId;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String CreateAsset(Context ctx, String assetId, String name, String color, int size) {
        ChaincodeStub stub = ctx.getStub();

        Asset newAsset = new Asset();
        newAsset.AssetID = assetId;
        newAsset.Name = name;
        newAsset.Color = color;
        newAsset.Size = size;

        String sortedJson = genson.serialize(newAsset);

        return assetId;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void mappingOwnerAsset(Context ctx, String assetId, String ownerId) {
        ChaincodeStub stub = ctx.getStub();
        stub.putStringState(ownerId, assetId);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryAssetOwner(Context ctx, String ownerId) {
        ChaincodeStub stub = ctx.getStub();
        return stub.getStringState(ownerId);
    }

    
}
