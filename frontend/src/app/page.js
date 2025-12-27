"use client"; // Δηλώνει ότι αυτό το component τρέχει στον browser (Client Side)

import { useState, useEffect } from "react";
import Link from "next/link";

export default function Home() {
  const [recipes, setRecipes] = useState([]); // Εδώ θα αποθηκεύσουμε τις συνταγές
  const [loading, setLoading] = useState(true);

  // 1. Λήψη δεδομένων από το Backend μόλις φορτώσει η σελίδα
  useEffect(() => {
    fetch("http://localhost:8081/api/recipes")
      .then((res) => res.json())
      .then((data) => {
        setRecipes(data);
        setLoading(false);
      })
      .catch((err) => console.error("Error fetching recipes:", err));
  }, []);

  // 2. Συνάρτηση Διαγραφής
  const handleDelete = async (id) => {
    if (confirm("Είσαι σίγουρος ότι θες να διαγράψεις αυτή τη συνταγή;")) {
      await fetch(`http://localhost:8081/api/recipes/${id}`, {
        method: "DELETE",
      });
      // Αφαιρούμε τη συνταγή από τη λίστα χωρίς να ξανακάνουμε refresh
      setRecipes(recipes.filter((recipe) => recipe.id !== id));
    }
  };

  if (loading)
    return <div className="text-center mt-10">Φόρτωση συνταγών...</div>;

  return (
    <div className="min-h-screen p-8 max-w-6xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">📖 Οι Συνταγές μου</h1>
        <Link href="/create">
          <button className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">
            + Νέα Συνταγή
          </button>
        </Link>
      </div>

      {/* Grid Λίστας Συνταγών */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {recipes.map((recipe) => (
          <div
            key={recipe.id}
            className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition"
          >
            {/* Εικόνα (αν υπάρχει, αλλιώς μια default) */}
            <div className="h-48 bg-gray-200 flex items-center justify-center">
              {/* Εδώ θα βάλουμε αργότερα τις πραγματικές εικόνες */}
              <span className="text-4xl">🥘</span>
            </div>

            <div className="p-5">
              <div className="flex justify-between items-start">
                <h2 className="text-xl font-bold mb-2">{recipe.name}</h2>
                <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                  {recipe.category}
                </span>
              </div>

              <div className="text-sm text-gray-600 mb-4 space-y-1">
                <p>⏱️ Χρόνος: {recipe.totalTime}'</p>
                <p>📊 Δυσκολία: {recipe.difficulty}</p>
              </div>

              <div className="flex justify-between mt-4">
                <Link href={`/recipe/${recipe.id}`}>
                  <button className="text-blue-600 hover:text-blue-800 font-medium">
                    Δείτε περισσότερα →
                  </button>
                </Link>
                <button
                  onClick={() => handleDelete(recipe.id)}
                  className="text-red-500 hover:text-red-700"
                >
                  🗑️
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {recipes.length === 0 && (
        <p className="text-center text-gray-500 mt-10">
          Δεν βρέθηκαν συνταγές. Πάτα το κουμπί για να προσθέσεις μία!
        </p>
      )}
    </div>
  );
}
