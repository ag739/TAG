var rooms = 
[
	{
		"id": "0",
		"description": "Somehow, it became your job to bring all the members of The Clique home for winter break so you can celebrate Cliquemas together. Type START to begin your adventure!",
		"items": [],
		"points": 0,
		"exits": [
			{
				"direction": "start",
				"room": "1"
			}
		],
		"treasure": []
    },
    {
    	"id": "1",
    	"description": "You finally finished your CS 3110 final, which means it's time to go home for winter break! When you walk out of your final, you find over 50 messages in your 'The Clique' groupme! You find out that your friends are stranded at their respective schools, and it is your responsibility to get everyone home. Luckily for you, you have free plane tickets to travel wherever you want! It's a long trip home, so you should probably PICK UP your SUITCASE and start to DRIVE home.",
    	"items": ["suitcase"],
    	"points": 0,
    	"exits": [
    		{
    			"direction": "drive",
    			"room": "2"
    		}
    	],
    	"treasure": []
    },
    {
    	"id": "2",
    	"description": "You're at the airport. There are several flights you can take. You can either go HOME, ITHACA, ALLENTOWN, MIAMI, or MINNEAPOLIS.",
    	"items": [],
    	"points": 10,
    	"exits": [
        	{
        		"direction": "home",
        		"room": "7"
        	},
	        {
	        	"direction": "ithaca",
	        	"room": "1"
	        },
	        {
	        	"direction": "allentown",
	        	"room": "3"
	        },
	        {
	        	"direction": "miami",
	        	"room": "5"
	        },
	        {
	        	"direction": "minneapolis",
	        	"room": "6"
	        }
    	],
    	"treasure": []
    },
    {
    	"id": "3",
		"description": "You've arrived at Muhlenberg College in Allentown, PA. How convienent it is that 2/5 of The Clique go to the same college!! You first go to Cassidy's room and you find out that Cassidy went abroad this semester! She asks if you could pick her up in Australia. Well, what friend wouldn't?! She also tells you that you shouldn't forget to pick up Kate.",
      	"items": ["Kate"],
      	"points": 10,
      	"exits": [
        	{
          		"direction": "airport",
          		"room": "2"
        	},
        	{
          		"direction": "australia",
          		"room": "4"
        	}
      	],
      	"treasure": []
    },
    {
    	"id": "4",
    	"description": "Wow, Australia is amazing! You begin to wonder why you never studied abroad, and then you remembered that CS majors just don't have the time for that. It's a long flight home, so you should probably pick up Cassidy and keep on going.",
		"items": ["Cassidy"],
    	"points": 10,
    	"exits": [
        	{
        	"direction": "airport",
        	"room": "2"
        	}
    	],
    	"treasure": []
    },
    {
    	"id": "5",
    	"description": "when you get to University of Miami, you beging to wonder why you chose to go to school in cold, snowy Ithaca. YOU COULD'VE WORN BATHING SUITS EVERY DAY! Anyways, you should probably go pick up Ali.",
    	"items": ["Ali"],
    	"points": 10,
    	"exits": [
        	{
        	"direction": "airport",
        	"room": "2"
        	}
      	],
      	"treasure": []
    },
    {
      	"id": "6",
      	"description": "You have arrived at the University of Minnesota! You now realize that there do exist other places that are just as cold as Ithaca in the winter time. You look down at your watch and see that it is time to pick up Nancy and get going.",
      	"items": ["Nancy"],
      	"points": 10,
      	"exits": [
        	{
          		"direction": "airport",
          		"room": "2"
        	}
      	],
      	"treasure": []
    },
    {
      	"id": "7",
      	"description": "You've made it back to Long Island! You have a bunch of things to do here. Mainly, you need to drop off all four of your friends from their respective colleges as well as drop of your suitcase that you brought home from Cornell. In case you missed anything, you could go back to the airport. Once you've done all of this, you can finally enjoy Cliquemas! (And in case you didn't know, Cliquemas is a time where all five members of The Clique -- Ariel, Ali, Cassidy, Kate, and Nancy -- get together before Christmas to do a small gift exchange.) Happy holidays!",
      	"items": [],
      	"points": 10,
      	"exits": [
        	{
          		"direction": "airport",
          		"room": "2"
        	}
      	],
      	"treasure": ["Ali", "Cassidy", "Kate", "Nancy", "suitcase"]
    }
];

var items =
  [
    {
      	"id": "Kate",
      	"description": "A member of The Clique",
      	"points": 100
    },
    {
      	"id": "Cassidy",
      	"description": "A member of The Clique",
      	"points": 100
    },
    {
      	"id": "Nancy",
      	"description": "A member of The Clique",
      	"points": 100
    },
    {
      	"id": "Ali",
      	"description": "A member of The Clique",
      	"points": 100
    },
    {
      	"id": "suitcase",
      	"description": "A bag with all of your clothes for winter break",
      	"points": 100
    }
];


var start_items = [];