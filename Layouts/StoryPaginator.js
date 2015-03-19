function storyToJSON_dynamic(storyTitle, storyContents){
	var jsondoc = {title:storyTitle,
		pages:[]
	};

	var clippedStory = storyContents;
	while(clippedStory.length > 0){
		var npE = getPageEnd(clippedStory);
		pages.push( clippedStory.slice(0, npE) );
		clippedStory = clippedStory.slice(npE);
	}

	return jsondoc;
}

function storyToJSON(storyTitle, storyContents){
	var jsondoc = {title:storyTitle,
		pages:[]
	};
	jsondoc.pages = getAllPages(storyContents);
	return jsondoc;
}

function getAllPages(storyContents, perPageSentences){
	var sentences = getAllSentences(storyContents);
	var retpages = [];
	var sindex;
	while(sindex < sentences.length){
		var pg = "";
		for(var i = 0; i < perPageSentences; i++){
			pg += " " + sentences[sindex].trim();
			sindex ++;
			if(sindex >= sentences.length){
				break;
			}
		}
		retpages.push(pg);
	}
	return retpages;
}

function getAllSentences(storyContents){
	return storyContents.split(/[\.\?\!]|(?=(---))/); // \s+[^.!?]*[.!?]
}

var force_pagebreak = "---";

function getPageEnd(cutStoryContents){
	return 100; // lolno
	// finds end of sentence
	// returns index of end of page
}

// pages designated by newlines
function simpleStoryToJSON(storyTitle, storyContents, pageDivider){
	var jsonthing = {};
	jsonthing.title = storyTitle;
	jsonthing.pages = [];
	divided = storyContents.split(pageDivider);
	for(var i = 0; i < divided.length; i++){
		jsonthing.pages.push(page(i+1, divided[i].trim()));
	}
	return jsonthing;
}

function page( index, content ){
	return { "index":index, "content":content };
}
