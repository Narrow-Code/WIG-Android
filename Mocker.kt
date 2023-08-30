package mockdata

import kotlin.random.Random

class Mocker {
	private val items = arrayOf("Oil Filter", "Keyboard", "Nailgun", "Nails", "Hammer", "Baby Clothes", "Chargers", "Hand Drill", "Wrench", "Spoons")
	private val brands = arrayOf("Dell", "KIA", "Great Value", "Valvoline", "Samsung", "Dewalt", "Stanley Black & Decker", "W.B.Mason", "ACE", "Home Depot", "Apple")
	private val bins = arrayOf("Gray Bin", "Blue Bin", "Bin 5", "Toolbox", "Shoebox", "Plastic Container", "Wood Crate", "Drawer 2", "Bucket", "Cardboard Box") 
	private val shelves = arrayOf("Shelf 1", "Wood Shelf 3", "AE-4", "DG-3", "Bookshelf 2", "Bakers Rack 2", "Lower Level", "Tinker Bar", "Rack 5", "7")
	private val locations = arrayOf("Basement", "Garage", "Storage Unit", "Closet", "Bedroom", "Library", "Kitchen", "Shed", "Attic", "Office")
	private val names = arrayOf("Matthew", "Mark", "Luke", "John", "Isaiah", "Jonah", "Daniel", "James", "Judas", "Paul")
	private val bags = arrayOf("Backpack", "Suitcase", "Breifcase", "Toolbag", "Bookbag", "Duffle Bag", "Messenger Bag", "Tote Bag", "Tackle Box", "Box")
	private val tags = arrayOf("Lawncare", "Car Maintenence", "Computer Stuff", "Pots & Pans", "Extra Clothing", "Old Stuff", "Cool Things", "Supplies", "Assets", "Consumables")
	private val types = arrayOf("Tool", "Electronic", "Kitchenware", "Clothes", "Books", "Movies", "Cleaning Supplies", "Food", "Bedding", "Art Supplies")
	private val qrs = arrayOf("stitchy-404", "solo-256", "spidey-558", "satiku-563", "stitchy-566", "solo-353", "spidey-534", "satiku-567", "stichy-222", "solo-678")
	private val itemImgs = arrayOf("https://mobileimages.lowes.com/productimages/0a7f39ef-8f14-4e08-b91a-7f046f0d8990/14644936.jpg", "https://m.media-amazon.com/images/I/61hM22zLE7S.jpg", "https://media.stokker.com/prod/l/691/159667691.jpg", "https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6477/6477858_sd.jpg")
	private val containerImgs = arrayOf("https://dijf55il5e0d1.cloudfront.net/images/rr/8/7/9/87948_1000.jpg", "https://images.thdstatic.com/productImages/86f3516e-a9a0-4bbd-b12c-2dffe7b19f86/svn/orange-edge-plastics-storage-bins-2020-11608-64_1000.jpg", "https://i1.wp.com/toolguyd.com/blog/wp-content/uploads/2018/04/Harbor-Freight-US-General-Series-2-Tool-Box.jpg")
	private val shelfImgs = arrayOf("https://m.media-amazon.com/images/I/813jI9uSE6L._AC_UF894,1000_QL80_.jpg", "https://www.oldhouseonline.com/review/wp-content/uploads/2023/02/basement-sheving-ohj.jpg", "https://www.miltonandking.com/wp-content/uploads/2018/10/Wallpaper-Kemra-Bookshelf-1-1100x1318.jpg")
	private val addresses = arrayOf("123 Sesame Street, New York, NY", "555 Warwick Ave, Warwick RI", "53 Park Ave, Fictional, WA", "532 Post Road, Chicago, MI", "8 Papertrail Lane, Egity, PA", "35 Hardstomp Ave, Garandor, NA")

	fun getAddress(): String{
		return addresses[Random.nextInt(addresses.size)]
	}

	fun getShelfImg(): String{
		return shelfImgs[Random.nextInt(shelfImgs.size)]
	}

	fun getContainerImg(): String{
		return containerImgs[Random.nextInt(containerImgs.size)]
	}

	fun getItemImg(): String{
		return itemImgs[Random.nextInt(itemImgs.size)]
	}

	fun getQR(): String{
		return qrs[Random.nextInt(qrs.size)]
	}


	fun getType(): String {
		return types[Random.nextInt(types.size)]
	}
	fun getTag(): String {
		return tags[Random.nextInt(tags.size)]
	}

	fun getBag(): String {
		return bags[Random.nextInt(bags.size)]
	}

	fun getName(): String {
		return names[Random.nextInt(names.size)]
	}

	fun getLocation(): String {
		return locations[Random.nextInt(locations.size)]
	}

	fun getItem(): String {
		return items[Random.nextInt(items.size)]
	}

	fun getBrand(): String {
		return brands[Random.nextInt(brands.size)]
	}

	fun getQuantity(): Int {
		return Random.nextInt(10) + 1
	}

	fun getUpc(): Long {
		val random = Random(System.currentTimeMillis())
		
		return random.nextLong(100000000000, 999999999999 + 1)
	}

	fun getBin(): String{ 
		return bins[Random.nextInt(bins.size)]
	}

	fun getShelf(): String {
		return shelves[Random.nextInt(shelves.size)]
	}

}



fun main() {
	println("Item: " + Mocker().getItem())
	println("Brand: " + Mocker().getBrand())
	println("Quantity: " + Mocker().getQuantity())
	println("UPC: " + Mocker().getUpc())
	println("Bin: " + Mocker().getBin())
	println("Shelf: " + Mocker().getShelf())
	println("Location: " + Mocker().getLocation())
	println("Name: " + Mocker().getName())
	println("Bag: " + Mocker().getBag())
	println("Tag: " + Mocker().getTag())
	println("Type: " + Mocker().getType())
	println("QR Code: " + Mocker().getQR())
	println("Item Image: " + Mocker().getItemImg())
	println("Container Image: " + Mocker().getContainerImg())
	println("Shelf Image: " + Mocker().getShelfImg())
	println("Address: " + Mocker().getAddress())
}
